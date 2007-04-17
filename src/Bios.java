import java.io.*;
import java.util.zip.*;

public class Bios {

 public Bios(String fname, int[] location) {
  try {
   loadFromFile(fname, location);
  }
  catch (IOException ioe) {
   System.out.println("Bios could not be loaded (message: " + ioe.getMessage() + ").");
   System.out.println("Emulator will go on like nothing happened.");
   location[0]=0xc3;
   location[1]=0x00;
   location[2]=0x01;
  }
 }

 public void loadFromFile(String fname, int[] location) throws IOException {

  long fsize = (new File(fname)).length();
  DataInputStream distream = FHandler.getDataStream(fname);

  for (int i = 0; i < fsize; ++i) {
   location[i] = distream.readUnsignedByte();
  }
 }
}
