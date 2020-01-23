
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.*;

public class Util {
	private FFmpegFrameRecorder recorder;
	private Scanner sc = new Scanner(System.in);

	public ArrayList<String> findOnlyImages(String path) {
		ArrayList<String> filelist = new ArrayList<String>();
		for(File file : new File(path).listFiles())
			if(file.isFile())
				if(isImage(file)) 
					filelist.add(file.getPath());
		return filelist;
	}

	public int noneImages(File folder) {
		int noneImages = 0;
		for(File file : folder.listFiles())
			if(file.isFile())
				if(!isImage(file)) 
					noneImages++;
		return noneImages;
	}

	private boolean isImage(File file) {
		String extension = getExtension(file.getName());
		return (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg") || extension.equals("Exif") || extension.equals("TIFF") || extension.equals("BMP"));
	}

	public String getExtension(String filename) {
		return filename.substring(filename.indexOf(".")+1);
	}

	public String getVideoLength(String folder) {
		int s = (int) (findOnlyImages(folder).size()/recorder.getFrameRate());
		return String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, s % 60);
	}

	public String getVideoSize(File video) {
		double fileSize = video.length();
		int kb = 1024, mb = kb*1024, gb = mb*1024;
		if(fileSize>=gb)
			return (double)Math.round((double) (fileSize/gb)*100)/100 + " GB"; 
		else if (fileSize>=mb)
			return (double)Math.round((double) (fileSize/mb)*100)/100 + " MB";
		else if (fileSize>=kb)
			return (double)Math.round((double) (fileSize/kb)*100)/100 + " kB";
		else
			return fileSize + " B";
	}

	public void toVideo(String folder, String videoPath, int fps)  {
		ArrayList<String> images = findOnlyImages(folder);
		int width=getWidth(images.get(0)), height=getHeight(images.get(0));
		OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
		recorder = new FFmpegFrameRecorder(videoPath,width,height);
		try {  
			recorder.setFrameRate(fps);  
			recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);   
			recorder.setVideoBitrate(10000);  
			recorder.setFormat("mp4");  
			recorder.setVideoQuality(0); 
			recorder.start();  
			System.out.println();
			for (int i=0;i<images.size();i++)  {
				int j = i+1;
				recorder.record(grabberConverter.convert(toIplImage(images.get(i)))); 
				System.out.print(progressbar(j, images.size())+"\r");
			}
			recorder.stop();  
		}  
		catch (org.bytedeco.javacv.FrameRecorder.Exception | IOException e){  
			e.printStackTrace();  
		}  
	}  

	public int getWidth(String file) {
		return new ImageIcon(file).getIconWidth();
	}
	public int getHeight(String file) {
		return new ImageIcon(file).getIconHeight();
	}
	private IplImage toIplImage(String path) throws IOException {
		return new OpenCVFrameConverter.ToIplImage().convert(new Java2DFrameConverter().convert(ImageIO.read(new File(path))));
	}
	private static String progressbar(int curr, int max) {
		double percentage = (double)Math.round(((double)curr/max)*100);
		String progress = percentage+"% [";
		for (int i = 0; i < 20; i++) 
			if (percentage/5 == 20 || i < (percentage/5)-1) progress+="=";
			else progress+=" ";
		progress+="] ["+curr+" of "+max+" images encoded]";
		return progress;
	}
	public boolean overwriteFile() {
		System.out.print("File already exists. Do you want to overwrite it? [Y/N] ");
		return sc.nextLine().toUpperCase().equals("Y");
	}
	public boolean fileExists(String path) {
		return new File(path).exists();
	}

	public static void main(String[] args) {
		for(int i=1;i<=100;i++)
			System.out.println(progressbar(i, 100));
	}
}  

