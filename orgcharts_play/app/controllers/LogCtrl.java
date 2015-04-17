package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import org.slf4j.LoggerFactory;
import play.api.Play;
import play.mvc.*;
import security.OrgChartRoleType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.List;

public class LogCtrl extends Controller{
    final static String LOG_FILE_NAME = "orgchart.log";

    @Restrict({@Group(OrgChartRoleType.ADMIN)})
    public static Result getLog(String fileName){
        response().setContentType("text/html");
        StringBuilder log = new StringBuilder();
        String logPath;
        if(null!=System.getProperty("catalina.base")||false!="null".equals(System.getProperty("catalina.base"))){
            logPath = System.getProperty("catalina.base") + "/logs/";
        }else{
            logPath = "./logs/";
        }

        Logger logger = (Logger) play.Logger.underlying();

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//        List<Logger> loggerList = lc.getLoggerList();
//        for(Logger l : loggerList){
//            log.append(l.toString()+"\n");
//        }

        boolean flagResetLogSetting = true;
        if(flagResetLogSetting){
            FileAppender<ILoggingEvent> fileAppender =
                    (FileAppender<ILoggingEvent>) logger.getAppender("play");
            if(fileAppender != null) {
                fileAppender.stop();
                fileAppender.setFile(logPath+LOG_FILE_NAME);
                log.append("Reset log file path=" + fileAppender.getFile());
//            PatternLayout pl = new PatternLayout();
//            pl.setPattern("%d %5p %t [%c:%L] %m%n)");
//            pl.setContext(lc);
//            pl.start();
//            fileAppender.setLayout(pl);
                fileAppender.setContext(lc);
                fileAppender.start();
            }
//        else{
//            log.append("fileAppender == null, creating one.\n");
//            FileAppender newFileAppender = new FileAppender();
//            newFileAppender.setFile(logPath+LOG_FILE_NAME);
//            logger.addAppender(newFileAppender);
//        }
        }

        File logFolder = new File(logPath);
        File[] listOfFiles = logFolder.listFiles();
        for (int i = 0; null!=listOfFiles && i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                log.append("<div>File <a href=\""+listOfFiles[i].getName()+"\">"+listOfFiles[i].getName()+"</a>" +
                        " <a href=\"del\\"+listOfFiles[i].getName()+"\">del</a>" +
                        "</div>");
            } else if (listOfFiles[i].isDirectory()) {
                log.append("Directory " + listOfFiles[i].getName());
            }
            log.append("\n");
        }

        if(null!=fileName&&false==fileName.equals("")){
            try (BufferedReader br = new BufferedReader(new FileReader(logPath+fileName))) {
                String line;
                if(null==br){
                    log.append("Log file error on read. Path="+logPath+fileName);
                }
                while ((line = br.readLine()) != null) {
                    log.append("<p>"+line+"</p>");
                }
            }catch (Exception e){
                e.printStackTrace();
                log.append("Log file not found. Path="+logPath+fileName);
                log.append("\n");
                return notFound(log.toString());
            }
        }
        return ok(log.toString());
    }


    @Restrict({@Group(OrgChartRoleType.ADMIN)})
    public static Result doDelLog(String fileName) {
        response().setContentType("text/html");
        StringBuilder log = new StringBuilder();
        String logPath;
        if (null != System.getProperty("catalina.base") || false != "null".equals(System.getProperty("catalina.base"))) {
            logPath = System.getProperty("catalina.base") + "/logs/";
        } else {
            logPath = "./logs/";
        }

        if(null!=fileName&&false==fileName.equals("")){
            try  {
                File file = new File(logPath+fileName);
                file.delete();
            }catch (Exception e){
                e.printStackTrace();
                log.append("doDelLog: Log file not found. Path="+logPath+fileName);
                log.append("\n");
                return notFound(log.toString());
            }
        }
        return ok(logPath+fileName+" DELETED.");
    }


    @Restrict({@Group(OrgChartRoleType.ADMIN)})
    public static Result getConfig(String fileName){
        response().setContentType("text/html");
        StringBuilder log = new StringBuilder();
        String logPath;
        if(null!=System.getProperty("catalina.base")||false!="null".equals(System.getProperty("catalina.base"))){
            logPath = System.getProperty("catalina.base") + "/conf/";
        }else{
            logPath = "./conf/";
        }

        Logger logger = (Logger) play.Logger.underlying();

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//        List<Logger> loggerList = lc.getLoggerList();
//        for(Logger l : loggerList){
//            log.append(l.toString()+"\n");
//        }

        boolean flagResetLogSetting = false;
        if(flagResetLogSetting){
            FileAppender<ILoggingEvent> fileAppender =
                    (FileAppender<ILoggingEvent>) logger.getAppender("play");
            if(fileAppender != null) {
                fileAppender.stop();
                fileAppender.setFile(logPath+LOG_FILE_NAME);
                log.append("Reset conf file path=" + fileAppender.getFile());
//            PatternLayout pl = new PatternLayout();
//            pl.setPattern("%d %5p %t [%c:%L] %m%n)");
//            pl.setContext(lc);
//            pl.start();
//            fileAppender.setLayout(pl);
                fileAppender.setContext(lc);
                fileAppender.start();
            }
//        else{
//            log.append("fileAppender == null, creating one.\n");
//            FileAppender newFileAppender = new FileAppender();
//            newFileAppender.setFile(logPath+LOG_FILE_NAME);
//            logger.addAppender(newFileAppender);
//        }
        }

        File logFolder = new File(logPath);
        File[] listOfFiles = logFolder.listFiles();

        if(null!=fileName&&false==fileName.equals("")){
            response().setContentType("text/plain");
            try (BufferedReader br = new BufferedReader(new FileReader(logPath+fileName))) {
                String line;
                if(null==br){
                    log.append("Conf file error on read. Path="+logPath+fileName);
                }
                while ((line = br.readLine()) != null) {
                    log.append(line+"\n");
                }
            }catch (Exception e){
                e.printStackTrace();
                log.append("Conf file not found. Path="+logPath+fileName);
                log.append("\n");
                return notFound(log.toString());
            }
        }else{

            for (int i = 0; null!=listOfFiles && i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    log.append("<div>File <a href=\""+listOfFiles[i].getName()+"\">"+listOfFiles[i].getName()+"</a></div>");
                } else if (listOfFiles[i].isDirectory()) {
                    log.append("Directory " + listOfFiles[i].getName());
                }
                log.append("\n");
            }
        }
        return ok(log.toString());
    }
}
