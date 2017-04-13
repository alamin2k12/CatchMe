import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class RichPanel extends JPanel
{
	private ArrayList<Image> backgrounds;
	private File directory;
	private Timer timer;
	private int timerDelay;
	private Image currentImage;
	private int currentImageIndex;

	public RichPanel(File folder)
	{
		super();

		backgrounds = new ArrayList<>();
		timerDelay = 3000;
		currentImage = null;
		currentImageIndex = -1;
		directory = folder;

		Load();
	}

	public ArrayList<Image> getBackgrounds()
	{
		return backgrounds;
	}

	public void setBackgrounds(ArrayList<Image> backgrounds)
	{
		this.backgrounds = backgrounds;
	}

	public File getDirectory()
	{
		return directory;
	}

	public void setDirectory(File directory)
	{
		this.directory = directory;
	}

	public int getTimerDelay()
	{
		return timerDelay;
	}

	public void setTimerDelay(int timerDelay)
	{
		this.timerDelay = timerDelay;
	}

	void Load()
	{
		File[] files = directory.listFiles();

		for (File file : files)
		{
			if (file.isFile() && ( getFileExtensionName(file).equals("jpg") || getFileExtensionName(file).equals("png") || getFileExtensionName(file).equals("gif")))
			{
				Image image = null;

				try
				{
					image = ImageIO.read(file);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}

				backgrounds.add(image);
			}
		}
	}

	public void startSlideShow()
	{
		if (timer == null)
		{
			timer = new Timer(timerDelay, new ActionListener() {

				public void actionPerformed(ActionEvent e)
				{
					currentImageIndex++;

					if (currentImageIndex >= backgrounds.size())
					{
						currentImageIndex = 0;
					}

					currentImage = backgrounds.get(currentImageIndex);

					paintImmediately(getBounds());
				}
			});

			currentImageIndex = 0;
			currentImage = backgrounds.get(currentImageIndex);
		}

		timer.start();
	}

	void stopSlideShow()
	{
		if (timer == null)
		{
			return;
		}

		timer.stop();
	}

	public static String getFileExtensionName(File f)
	{
		int dot = f.getName().indexOf('.');

		if (f.getName().indexOf(".") == -1)
		{
			return "";
		}

		return f.getName().substring(dot + 1);
	}

	protected void paintComponent(Graphics g)
	{
//		g.drawImage(currentImage, 0, 0, null);
		g.drawImage(currentImage, 0, 0, getWidth(), getHeight(), null);

	}

}
