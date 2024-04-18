package com.ljiaping.usercenter.service;

import com.ljiaping.usercenter.exception.BusinessException;
import com.ljiaping.usercenter.model.AccessInfo;
import com.ljiaping.usercenter.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final String TEST_FILE_PATH = "test_access.txt";
    private static final long TEST_USER_ID_1 = 1L;
    private static final String TEST_ENDPOINT_1 = "endpoint1";
    private static final String TEST_ENDPOINT_2 = "endpoint2";
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() throws Exception {
        // Set the file path for the test
        ReflectionTestUtils.setField(userService, "filePath", TEST_FILE_PATH);
        // Ensure the file is deleted before each test
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Clean up the test file after each test
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testUpsertAccessInfo_NewUser() throws IOException {
        AccessInfo accessInfo = new AccessInfo(TEST_USER_ID_1, Collections.singletonList(TEST_ENDPOINT_1));
        userService.upsertAccessInfo(accessInfo);

        List<String> lines = Files.readAllLines(Paths.get(TEST_FILE_PATH));
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains(TEST_USER_ID_1 + ":" + TEST_ENDPOINT_1));
    }

    @Test
    public void testUpsertAccessInfo_ExistingUser() throws IOException {
        // Prepare an initial entry
        String initialEntry = TEST_USER_ID_1 + ":" + TEST_ENDPOINT_1;
        Files.write(Paths.get(TEST_FILE_PATH), Collections.singletonList(initialEntry));

        // Update the user's access info
        AccessInfo updatedAccessInfo = new AccessInfo(TEST_USER_ID_1, Arrays.asList(TEST_ENDPOINT_1, TEST_ENDPOINT_2));
        userService.upsertAccessInfo(updatedAccessInfo);

        List<String> lines = Files.readAllLines(Paths.get(TEST_FILE_PATH));
        assertEquals(1, lines.size());
        String expectedEntry = TEST_USER_ID_1 + ":" + TEST_ENDPOINT_1 + "," + TEST_ENDPOINT_2;
        assertEquals(expectedEntry, lines.get(0));
    }

    @Test
    public void testFindAccessInfoById_UserNotFound() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.findAccessInfoById(TEST_USER_ID_1);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testFindAccessInfoById_UserFound() throws IOException, BusinessException {
        // Prepare an entry for the user
        String userEntry = TEST_USER_ID_1 + ":" + TEST_ENDPOINT_1;
        Files.write(Paths.get(TEST_FILE_PATH), Collections.singletonList(userEntry));

        // Find the user's access info
        AccessInfo foundAccessInfo = userService.findAccessInfoById(TEST_USER_ID_1);

        assertNotNull(foundAccessInfo);
        assertEquals(TEST_USER_ID_1, foundAccessInfo.getUserId());
        assertEquals(Collections.singletonList(TEST_ENDPOINT_1), foundAccessInfo.getEndpoints());
    }
}