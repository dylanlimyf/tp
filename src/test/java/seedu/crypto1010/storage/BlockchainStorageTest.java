package seedu.crypto1010.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import seedu.crypto1010.model.Blockchain;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlockchainStorageTest {
    private static final Path DATA_DIR = Path.of(System.getProperty("user.dir"), "data");
    private static final Path BLOCKCHAIN_FILE = DATA_DIR.resolve("blockchain.json");

    private String originalBlockchainContent;
    private boolean hadOriginalBlockchainFile;

    @BeforeEach
    void backupExistingBlockchainFile() throws IOException {
        hadOriginalBlockchainFile = Files.exists(BLOCKCHAIN_FILE);
        if (hadOriginalBlockchainFile) {
            originalBlockchainContent = Files.readString(BLOCKCHAIN_FILE, StandardCharsets.UTF_8);
        }
    }

    @AfterEach
    void restoreBlockchainFile() throws IOException {
        if (hadOriginalBlockchainFile) {
            Files.createDirectories(BLOCKCHAIN_FILE.getParent());
            Files.writeString(BLOCKCHAIN_FILE, originalBlockchainContent, StandardCharsets.UTF_8);
        } else {
            Files.deleteIfExists(BLOCKCHAIN_FILE);
        }
    }

    @Test
    void saveThenLoad_persistsBlockchainData() throws IOException {
        BlockchainStorage storage = new BlockchainStorage(BlockchainStorageTest.class);
        Blockchain blockchain = Blockchain.createDefault();

        storage.save(blockchain);
        Blockchain loaded = storage.load();

        assertEquals(blockchain.size(), loaded.size());
        assertEquals(blockchain.getBlock(0).getCurrentHash(), loaded.getBlock(0).getCurrentHash());
        assertEquals(blockchain.getBlock(1).getCurrentHash(), loaded.getBlock(1).getCurrentHash());
        assertEquals(blockchain.getBlock(1).getTransactions(), loaded.getBlock(1).getTransactions());
        assertTrue(loaded.validate().isValid());
    }

    @Test
    void load_missingFile_returnsDefaultBlockchain() throws IOException {
        Files.deleteIfExists(BLOCKCHAIN_FILE);
        BlockchainStorage storage = new BlockchainStorage(BlockchainStorageTest.class);

        Blockchain loaded = storage.load();

        assertEquals(2, loaded.size());
        assertTrue(loaded.validate().isValid());
    }

    @Test
    void load_invalidBlockchain_throwsIOException() throws IOException {
        Files.createDirectories(DATA_DIR);
        String invalidBlockchainJson = """
                {
                  "blocks": [
                    {
                      "index": 0,
                      "timestamp": "2026-02-12 14:30:21",
                      "previousHash": "0000000000000000",
                      "currentHash": "invalidhash",
                      "transactions": ["Genesis Block"]
                    }
                  ]
                }
                """;
        Files.writeString(BLOCKCHAIN_FILE, invalidBlockchainJson, StandardCharsets.UTF_8);
        BlockchainStorage storage = new BlockchainStorage(BlockchainStorageTest.class);

        IOException exception = assertThrows(IOException.class, storage::load);
        assertTrue(exception.getMessage().startsWith("Loaded blockchain is invalid:"));
    }
}
