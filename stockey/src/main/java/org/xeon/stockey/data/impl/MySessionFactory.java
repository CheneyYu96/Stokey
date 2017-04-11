package org.xeon.stockey.data.impl;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class MySessionFactory {
	private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if(MySessionFactory.sessionFactory==null)
            MySessionFactory.setUp();
        return MySessionFactory.sessionFactory;
    }

    protected static void setUp(){
		// A SessionFactory is set up once for an application!
		File f = new File("./hibernate/hibernate.cfg.xml");
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure(f) // configures settings from hibernate.cfg.xml
				.build();
		try {
			sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception happened!!!");
			// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
			// so destroy it manually.
			StandardServiceRegistryBuilder.destroy( registry );
		}
	}
}
