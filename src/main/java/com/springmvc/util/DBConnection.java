package com.springmvc.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import javax.swing.*;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;

import com.springmvc.configuration.HibernateConfiguration;

public class DBConnection {
	static Logger log = Logger.getLogger(DBConnection.class.getName());
//	public static boolean instrumentData = true;
//	public static String defaultValue = "ST4  TR23 1Sec     0.000 2           1S   9.500   0.000   0.000           2S   9.800   0.000   0.000o";
	Connection conn = null;

	public static Connection dbConnection() {
		log.info("Connection dbConnection() start");
		try {
			Class.forName(HibernateConfiguration.driver);
			Connection conn = DriverManager.getConnection(HibernateConfiguration.dbConnectionUrl,
					HibernateConfiguration.userName, HibernateConfiguration.password);
			conn.setSchema(HibernateConfiguration.schemaName);
			return conn;
		} catch (Exception e) {
			String message = "";
			for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
				message = message + System.lineSeparator() + stackTraceElement.toString();
			}
			log.error(message);
			log.error(e);
			log.error(e.fillInStackTrace());
			log.error(e.getMessage());
			JOptionPane.showConfirmDialog(null, "DB Connection Failed. Please, contact Support");
			JOptionPane.showConfirmDialog(null, "1" + e);
			return null;
		}
	}
}
