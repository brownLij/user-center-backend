package com.ljiaping.usercenter.service.impl;

import com.ljiaping.usercenter.common.ErrorCode;
import com.ljiaping.usercenter.exception.BusinessException;
import com.ljiaping.usercenter.model.AccessInfo;
import com.ljiaping.usercenter.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ljiaping.usercenter.Constants.COLON_STR;
import static com.ljiaping.usercenter.Constants.COMMA_STR;

@Service
public class UserServiceImpl implements UserService {

    @Value("${access.file.path}")
    private String filePath;

    @Override
    public void upsertAccessInfo(AccessInfo userAccess) {
        try {
            List<String> existingEntries = Files.readAllLines(Paths.get(filePath));
            List<String> updatedEntries = new ArrayList<>();
            boolean userFound = false;

            for (String entry : existingEntries) {
                String[] parts = entry.split(COLON_STR);
                Long userId = Long.parseLong(parts[0].trim());
                if (userId.equals(userAccess.getUserId())) {
                    // User found, update the entry
                    userFound = true;
                    updatedEntries.add(userAccess.getUserId() + COLON_STR + String.join(COMMA_STR, userAccess.getEndpoints()));
                } else {
                    // Keep other entries as they are
                    updatedEntries.add(entry);
                }
            }

            if (!userFound) {
                // Add a new entry if the user was not found
                updatedEntries.add(userAccess.getUserId() + COLON_STR + String.join(COMMA_STR, userAccess.getEndpoints()));
            }

            // Write updated entries back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String entry : updatedEntries) {
                    writer.write(entry);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public AccessInfo findAccessInfoById(long id) {
        try {
            // Read the access file and find the line for the given userId
            List<String> userAccessLines = Files.readAllLines(Paths.get(filePath));
            String userAccessLine = userAccessLines.stream()
                    .filter(entry -> entry.startsWith(id + COLON_STR))
                    .findFirst()
                    .orElse(null);

            if (userAccessLine != null) {
                return convertToAccessInfo(userAccessLine);
            } else {
                // User not found in the access file
                throw new BusinessException(ErrorCode.USER_NOT_FOUND);
            }
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    private AccessInfo convertToAccessInfo(String AccessInfoLine) {
        String[] parts = AccessInfoLine.split(COLON_STR);
        long userId = Long.parseLong(parts[0].trim());
        String[] endpointsArray = parts[1].split(COMMA_STR);
        List<String> resources = Arrays.asList(endpointsArray);
        return new AccessInfo(userId, resources);
    }
}
