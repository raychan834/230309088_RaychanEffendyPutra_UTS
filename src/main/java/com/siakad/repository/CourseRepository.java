package com.siakad.repository;

import com.siakad.model.Course;

/**
 * Interface untuk akses data mata kuliah
 * Interface ini akan di-stub atau di-mock dalam unit testing
 */

public class CourseRepository {

    /**
     * Mencari mata kuliah berdasarkan course code
     *
     * @param courseCode Kode mata kuliah
     * @return Course object atau null jika tidak ditemukan
     */
    public Course findByCourseCode(String courseCode) {
        return null;
    }

    /**
     * Update data mata kuliah
     *
     * @param course Course object yang akan diupdate
     */
    public void update(Course course) {

    }

    /**
     * Mengecek apakah prasyarat mata kuliah sudah terpenuhi
     *
     * @param studentId  ID mahasiswa
     * @param courseCode Kode mata kuliah
     * @return true jika prasyarat terpenuhi, false jika tidak
     */
    public boolean isPrerequisiteMet(String studentId, String courseCode) {
        return false;
    }
}