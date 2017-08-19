package org.techforumist.keynote;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.techforumist.keynote.processor.RestrictedSizeInputStream;
import org.techforumist.keynote.processor.SnappyNoCRCFramedInputStream;
import org.techforumist.keynote.proto.KN.KNArchives.DocumentArchive;
import org.techforumist.keynote.proto.KN.KNArchives.NoteArchive;
import org.techforumist.keynote.proto.KN.KNArchives.PlaceholderArchive;
import org.techforumist.keynote.proto.KN.KNArchives.SlideArchive;
import org.techforumist.keynote.proto.KN.KNArchives.SlideNodeArchive;
import org.techforumist.keynote.proto.TSD.TSDArchives.GroupArchive;
import org.techforumist.keynote.proto.TSP.TSPArchiveMessages.ArchiveInfo;
import org.techforumist.keynote.proto.TSP.TSPArchiveMessages.MessageInfo;
import org.techforumist.keynote.proto.TST.TSTArchives;
import org.techforumist.keynote.proto.TSWP.TSWPArchives.PlaceholderSmartFieldArchive;
import org.techforumist.keynote.proto.TSWP.TSWPArchives.ShapeInfoArchive;
import org.techforumist.keynote.proto.TSWP.TSWPArchives.StorageArchive;

import com.google.protobuf.Descriptors.FieldDescriptor;

public class Main {


    public static void main(String[] args) {
        process();
        // org.techforumist.keynote.proto.*
    }

    public static void process() {
        try {
            String path1 = "D:/Work/Workspace/kyenote-01/keynote/input/sample/Index/MasterSlide-1.iwa";
            String path2 = "D:/Work/Workspace/kyenote-01/keynote/input/testKeynote2013/Index/Slide-1.iwa";
            String path3 = "D:/Work/Workspace/kyenote-01/keynote/input/testKeynote2013/Index/Slide.iwa";
            String path4 = "D:/Work/Workspace/kyenote-01/keynote/input/testKeynote2013/Index/Slide-2.iwa";
            File file = new File(path4);
            final InputStream bin = new SnappyNoCRCFramedInputStream(new FileInputStream(file), false);
            final RestrictedSizeInputStream rsIn = new RestrictedSizeInputStream(bin, 0);
            while (!Thread.interrupted()) {
                try {
                    ArchiveInfo ai = ArchiveInfo.parseDelimitedFrom(bin);
                    if (ai != null) {
                        for (MessageInfo mi : ai.getMessageInfosList()) {
                            Map<FieldDescriptor, Object> allFields = mi.getAllFields();
                            //System.out.println(allFields);
                            try {
                                // mi.getLength());
                                rsIn.setNumBytesReadable(mi.getLength());
                                Object o = null;
                                String element = "";
                                switch (mi.getType()) {
                                    case 6005:
                                    case 6201:
                                        TSTArchives.TableDataList tableDataList = TSTArchives.TableDataList.parseFrom(rsIn);
                                        o = tableDataList;
                                        element = "tableDataList";
                                        break;

                                    case 2001:
                                        StorageArchive storageArchive = StorageArchive.parseFrom(rsIn);
                                        o = storageArchive;
                                        element = "StorageArchive";
                                        break;
                                    case 2031:
                                        PlaceholderSmartFieldArchive placeholderSmartFieldArchive = PlaceholderSmartFieldArchive.parseFrom(rsIn);
                                        o = placeholderSmartFieldArchive;
                                        element = "placeholderSmartFieldArchive";
                                        break;

                                    case 3008:
                                        GroupArchive groupArchive = GroupArchive.parseFrom(rsIn);
                                        o = groupArchive;
                                        element = "groupArchive";
                                        break;

                                    case 1:
                                        DocumentArchive documentArchive = DocumentArchive.parseFrom(rsIn);
                                        o = documentArchive;
                                        element = "documentArchive";
                                        break;
                                    case 4:
                                        SlideNodeArchive slideNodeArchive = SlideNodeArchive.parseFrom(rsIn);
                                        o = slideNodeArchive;
                                        element = "slideNodeArchive";
                                        break;
                                    case 5:
                                    case 6:
                                        SlideArchive slideArchive = SlideArchive.parseFrom(rsIn);
                                        o = slideArchive;
                                        element = "slideArchive";
                                        break;
                                    case 7:
                                        PlaceholderArchive placeholderArchive = PlaceholderArchive.parseFrom(rsIn);
                                        o = placeholderArchive;
                                        element = "placeholderArchive";
                                        break;

                                    case 15:
                                        NoteArchive noteArchive = NoteArchive.parseFrom(rsIn);
                                        o = noteArchive;
                                        element = "noteArchive";
                                        break;
                                    case 2011:
                                        ShapeInfoArchive shapeInfoArchive = ShapeInfoArchive.parseFrom(rsIn);
                                        o = shapeInfoArchive;
                                        element = "shapeInfoArchive";
                                        break;

                                    default:
                                        break;
                                }
                                System.out.println(element + " >> " + mi.getType());


                                // StorageArchive message =
                                // StorageArchive.parseFrom(rsIn);
                                // SlideArchive slide =
                                // SlideArchive.parseFrom(rsIn);
                                // System.out.println(slide);
                                // System.out.println(slide.getInDocument());
                                // System.out.println("------------------------------------------------");
                                // Map<FieldDescriptor, Object> allFields =
                                // slide.getAllFields();
                                // for (FieldDescriptor f : allFields.keySet())
                                // {
                                // System.out.println(f.getFullName() + " >>
                                // " + allFields.get(f).getClass());

                                // if (f.getJsonName().equals("note")) {
                                // System.out.println(allFields.get(f).getClass());
                                // }
                                // }
                                // for (int i = 0; i < message.getTextCount();
                                // i++) {
                                // System.out.println(mi.getType() + " >>> " +
                                // message.getText(i));
                                // }
                            } catch (Exception e) {
                                // e.printStackTrace();
                            } finally {
                                rsIn.skipRest();
                            }
                        }
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
