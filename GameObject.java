import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class GameObject extends JLabel
{
	private String name;
	private Image gameIcon;
	
	
	public GameObject()
	{
		this.name = null;
		this.gameIcon = null;
	}
	
	public GameObject(String name, Image gameIcon)
	{
		super();
		
		this.name = name;
		this.gameIcon = gameIcon;
		
		setBorder(null);
		setSize(100, 100);
		setIcon(new ImageIcon(gameIcon));
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Image getGameIcon()
	{
		return gameIcon;
	}

	public void setGameIcon(Image gameIcon)
	{
		this.gameIcon = gameIcon;
	}
}
