package org.techforumist.keynote;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.techforumist.keynote.processor.RestrictedSizeInputStream;
import org.techforumist.keynote.processor.SnappyNoCRCFramedInputStream;
import org.techforumist.keynote.proto.KN.KNArchives.SlideArchive;
import org.techforumist.keynote.proto.TSD.TSDArchives.GroupArchive;
import org.techforumist.keynote.proto.TSP.TSPArchiveMessages.ArchiveInfo;
import org.techforumist.keynote.proto.TSP.TSPArchiveMessages.MessageInfo;
import org.techforumist.keynote.proto.TST.TSTArchives;
import org.techforumist.keynote.proto.TSWP.TSWPArchives.PlaceholderSmartFieldArchive;
import org.techforumist.keynote.proto.TSWP.TSWPArchives.StorageArchive;

import com.google.protobuf.Descriptors.FieldDescriptor;

public class Main {
	public static void main(String[] args) {
		try {

			String path1 = "D:/Work/Workspace/kyenote-01/keynote/input/sample/Index/MasterSlide-1.iwa";
			String path2 = "D:/Work/Workspace/kyenote-01/keynote/input/testKeynote2013/Index/Slide-1.iwa";
			String path3 = "D:/Work/Workspace/kyenote-01/keynote/input/testKeynote2013/Index/Slide.iwa";
			File file = new File(path3);
			final InputStream bin = new SnappyNoCRCFramedInputStream(new FileInputStream(file), false);
			final RestrictedSizeInputStream rsIn = new RestrictedSizeInputStream(bin, 0);
			while (!Thread.interrupted()) {
				try {
					ArchiveInfo ai = ArchiveInfo.parseDelimitedFrom(bin);
					if (ai != null) {
						for (MessageInfo mi : ai.getMessageInfosList()) {
							try {
								// mi.getLength());
								rsIn.setNumBytesReadable(mi.getLength());
								Object o = null;
								String element = "";
								switch (mi.getLength()) {
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

								default:
									break;
								}
								System.out.println(element + " >> ");
								System.out.println(mi.getType() + " : " + o + "\n");
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
