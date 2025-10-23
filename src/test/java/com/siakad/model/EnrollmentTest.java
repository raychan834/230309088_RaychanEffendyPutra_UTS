package com.siakad.model;

// Impor anotasi JUnit
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
// Impor kelas Java standar
import java.time.LocalDateTime;
// Impor method 'static' assertions
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tes ini untuk menguji model Enrollment.java.
 * Kita akan menguji kedua constructor dan semua getter/setter
 * untuk mencapai 100% coverage.
 */
class EnrollmentTest {

    @Test
    @DisplayName("Test Constructor Kosong, Getter, dan Setter")
    void testGettersAndSetters() {
        // 1. Arrange (Persiapan) & 2. Act (Eksekusi)

        // Panggil constructor kosong
        Enrollment enrollment = new Enrollment();

        // Buat data waktu
        LocalDateTime now = LocalDateTime.now();

        // Panggil semua setter
        enrollment.setEnrollmentId("ENR-001");
        enrollment.setStudentId("S001");
        enrollment.setCourseCode("CS-101");
        enrollment.setEnrollmentDate(now);
        enrollment.setStatus("APPROVED");

        // 3. Assert (Validasi)
        // Verifikasi semua getter
        assertAll("Verifikasi getter dan setter",
                () -> assertEquals("ENR-001", enrollment.getEnrollmentId()),
                () -> assertEquals("S001", enrollment.getStudentId()),
                () -> assertEquals("CS-101", enrollment.getCourseCode()),
                () -> assertEquals(now, enrollment.getEnrollmentDate()),
                () -> assertEquals("APPROVED", enrollment.getStatus())
        );
    }

    @Test
    @DisplayName("Test Constructor Lengkap (All-Args)")
    void testAllArgsConstructor() {
        // 1. Arrange (Persiapan)
        LocalDateTime now = LocalDateTime.now();

        // 2. Act (Eksekusi)
        // Panggil constructor lengkap
        Enrollment enrollment = new Enrollment(
                "ENR-002",
                "S002",
                "CS-201",
                now,
                "PENDING"
        );

        // 3. Assert (Validasi)
        // Cek semua field yang di-set oleh constructor
        assertEquals("ENR-002", enrollment.getEnrollmentId());
        assertEquals("S002", enrollment.getStudentId());
        assertEquals("CS-201", enrollment.getCourseCode());
        assertEquals(now, enrollment.getEnrollmentDate());
        assertEquals("PENDING", enrollment.getStatus());
    }
}