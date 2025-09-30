package org.example.dao;

import org.example.model.Course;
import org.hibernate.SessionFactory;

public class CourseDao extends GenericDaoImpl<Course, Long> {

    public CourseDao(SessionFactory sessionFactory, Class<Course> type) {
        super(sessionFactory, type);
    }
}

