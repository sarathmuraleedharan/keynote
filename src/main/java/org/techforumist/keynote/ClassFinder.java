package org.techforumist.keynote;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sarath Muraleedharan on 20-08-2017.
 */
public class ClassFinder {

    private static final char PKG_SEPARATOR = '.';

    private static final char DIR_SEPARATOR = '/';

    private static final String CLASS_FILE_SUFFIX = ".class";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

    public static List<Class<?>> find(String scannedPackage) {
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (File file : scannedDir.listFiles()) {
            classes.addAll(find(file, scannedPackage));
        }
        return classes;
    }

    public static void findMethods() {
        try {
            Class.forName("org.techforumist.keynote.proto.TSCH.Generated.TSCHArchivesGEN");
            Package[] pList = Package.getPackages();
            for (Package p : pList) {
                if (p.getName().startsWith("org.techforumist.keynote.proto")) {
                    //System.out.println(p.getName() + " >> ");
                    List<Class<?>> list = ClassFinder.find(p.getName());
                    for (Class c : list) {
                        try {
                            System.out.println(c.getName());
                            Method[] ms = c.getMethods();
                            for (Method m : ms) {
                                if (m.getName().equals("getReference")) {
                                    System.out.println(m.getName());
                                } else {
                                    System.out.println(" >> " + m.getName());
                                }
                            }
                            System.out.println();
                            // Method declaredMethod = c.getDeclaredMethod("getReference");
                            //System.out.println("\t" + c.getName() + "," + declaredMethod.invoke(null).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }

}
