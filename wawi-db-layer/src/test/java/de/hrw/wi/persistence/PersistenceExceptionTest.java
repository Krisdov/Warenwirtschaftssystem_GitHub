package de.hrw.wi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class PersistenceExceptionTest {

    private static final String INITIAL_CAUSE = "Initial cause";

    @Test
    void testPersistenceExceptionMessage() {
        RuntimeException e = new PersistenceException();
        try {
            throw e;
        } catch (PersistenceException eCaught) {
            return;
        }
        // fail here not necessary and treated as error by compiler
    }

    @Test
    void testPersistenceExceptionMessageCause() {
        final String message1 = "Test message";
        final String message2 = INITIAL_CAUSE;
        RuntimeException e2 = new PersistenceException(message2);
        RuntimeException e1 = new PersistenceException(message1, e2);
        try {
            throw e1;
        } catch (PersistenceException eCaught) {
            assertEquals(message1, eCaught.getMessage());
            assertEquals(e2, eCaught.getCause());
            assertEquals(message2, eCaught.getCause().getMessage());
            return;
        }
        // fail here not necessary and treated as error by compiler
    }

    @Test
    void testPersistenceExceptionCauseParameter() {
        final String message2 = INITIAL_CAUSE;
        RuntimeException e2 = new PersistenceException(message2);
        RuntimeException e1 = new PersistenceException(e2);
        try {
            throw e1;
        } catch (PersistenceException eCaught) {
            assertEquals(e2, eCaught.getCause());
            assertEquals(message2, eCaught.getCause().getMessage());
            return;
        }
        // fail here not necessary and treated as error by compiler
    }
    
    @Test
    @SuppressWarnings(value = { "null" })
    void testPersistenceExceptionMessageCauseWithSuppressFlag() {
        DatabaseReadInterface dbNotInitialized = null;
        try {
            dbNotInitialized.getAllProducts();
        } catch (PersistenceException pe) {
            fail("PersistenceException thrown while this should not be possible");
        } catch (NullPointerException npe) {
            final String message = "second exception";
            // case 1: suppressed exceptions enabled
            RuntimeException e1 = new PersistenceException(message, npe, true, true);
            e1.addSuppressed(npe);
            try {
                throw e1;
            } catch (PersistenceException eCaught) {
                assertEquals(message, eCaught.getMessage());
                assertEquals(npe, eCaught.getCause());
                assertEquals(npe, eCaught.getSuppressed()[0]);
                
                // case 2: suppressed exceptions disabled
                RuntimeException e2 = new PersistenceException(message, npe, false, true);
                e2.addSuppressed(npe);
                try {
                    throw e2;
                } catch (PersistenceException eCaught2) {
                    assertEquals(message, eCaught2.getMessage());
                    assertEquals(npe, eCaught2.getCause());
                    assertEquals(0, eCaught2.getSuppressed().length);
                    return;
                }
            }
        }
        // fail here not necessary and treated as error by compiler
    }
    
}
