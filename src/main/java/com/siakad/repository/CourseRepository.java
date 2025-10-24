package com.siakad.repository;

import com.siakad.model.Course;

/**
 * Interface untuk akses data mata kuliah
 * Interface ini akan di-stub atau di-mock dalam unit testing
 */

// DIUBAH: Menjadi interface
public interface CourseRepository {

    /**
     * Mencari mata kuliah berdasarkan course code
     * @param courseCode Kode mata kuliah
     * @return Course object atau null jika tidak ditemukan
     */
    // Method di interface otomatis public, tidak perlu 'abstract'
    Course findByCourseCode(String courseCode);

    /**
     * Update data mata kuliah
     * @param course Course object yang akan diupdate
     */
    void update(Course course);

    /**
     * Mengecek apakah prasyarat mata kuliah sudah terpenuhi
     * @param studentId ID mahasiswa
     * @param courseCode Kode mata kuliah
     * @return true jika prasyarat terpenuhi, false jika tidak
     */
    boolean isPrerequisiteMet(String studentId, String courseCode);
}