package org.example.repository;

import org.example.model.UserCourses;
import org.hibernate.SessionFactory;

public class UserCoursesRepository extends GenericRepositoryImpl<UserCourses, Long> {
    public UserCoursesRepository(SessionFactory sessionFactory, Class<UserCourses> type) {
        super(sessionFactory, type);
    }
}

