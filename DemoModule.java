/*
 * %W% %E%
 * 
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * Neither the name of Oracle or the names of contributors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL 
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST 
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY 
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, 
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */

/*
 * %W% %E%
 */

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.net.*;

/**
 * A generic SwingSet3 demo module
 *
 * @version %I% %G%
 * @author Jeff Dinkins
 */
public class DemoModule extends JApplet {

    // The preferred size of the demo
    private int PREFERRED_WIDTH = 680;
    private int PREFERRED_HEIGHT = 600;

    Border loweredBorder = new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), 
					      new EmptyBorder(5,5,5,5));

    // Premade convenience dimensions, for use wherever you need 'em.
    public static Dimension HGAP2 = new Dimension(2,1);
    public static Dimension VGAP2 = new Dimension(1,2);

    public static Dimension HGAP5 = new Dimension(5,1);
    public static Dimension VGAP5 = new Dimension(1,5);
    
    public static Dimension HGAP10 = new Dimension(10,1);
    public static Dimension VGAP10 = new Dimension(1,10);

    public static Dimension HGAP15 = new Dimension(15,1);
    public static Dimension VGAP15 = new Dimension(1,15);
    
    public static Dimension HGAP20 = new Dimension(20,1);
    public static Dimension VGAP20 = new Dimension(1,20);

    public static Dimension HGAP25 = new Dimension(25,1);
    public static Dimension VGAP25 = new Dimension(1,25);

    public static Dimension HGAP30 = new Dimension(30,1);
    public static Dimension VGAP30 = new Dimension(1,30);
	
    private SwingSet3 swingset = null;
    private JPanel panel = null;
    private String resourceName = null;
    private String iconPath = null;
    private String sourceCode = null;

    // Resource bundle for internationalized and accessible text
    private ResourceBundle bundle = null;

    public DemoModule(SwingSet3 swingset) {
	this(swingset, null, null);
    }

    public DemoModule(SwingSet3 swingset, String resourceName, String iconPath) {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
	panel = new JPanel();
	panel.setLayout(new BorderLayout());

	this.resourceName = resourceName;
	this.iconPath = iconPath;
	this.swingset = swingset;

	loadSourceCode();
    }

    public String getResourceName() {
	return resourceName;
    }

    public JPanel getDemoPanel() {
	return panel;
    }

    public SwingSet3 getSwingSet3() {
	return swingset;
    }


    public String getString(String key) {
	String value = "nada";
	if(bundle == null) {
	    if(getSwingSet3() != null) {
		bundle = getSwingSet3().getResourceBundle();
	    } else {
		bundle = ResourceBundle.getBundle("resources.swingset");
	    }
	}
	try {
	    value = bundle.getString(key);
	} catch (MissingResourceException e) {
	    System.out.println("java.util.MissingResourceException: Couldn't find value for: " + key);
	}
	return value;
    }

    public char getMnemonic(String key) {
	return (getString(key)).charAt(0);
    }

    public ImageIcon createImageIcon(String filename, String description) {
	if(getSwingSet3() != null) {
	    return getSwingSet3().createImageIcon(filename, description);
	} else {
	    String path = "/resources/images/" + filename;
	    return new ImageIcon(getClass().getResource(path), description); 
	}
    }
    

    public String getSourceCode() {
	return sourceCode;
    }

    public void loadSourceCode() {
	if(getResourceName() != null) {
	    String filename = getResourceName() + ".java";
	    sourceCode = new String("<html><body bgcolor=\"#ffffff\"><pre>");
	    InputStream is;
	    InputStreamReader isr;
	    CodeViewer cv = new CodeViewer();
	    URL url;
	    
	    try {
		url = getClass().getResource(filename); 
		is = url.openStream();
		isr = new InputStreamReader(is, "UTF-8");
		BufferedReader reader = new BufferedReader(isr);
		
		// Read one line at a time, htmlize using super-spiffy
		// html java code formating utility from www.CoolServlets.com
		String line = reader.readLine();
		while(line != null) {
		    sourceCode += cv.syntaxHighlight(line) + " \n ";
		    line = reader.readLine();
		}
		sourceCode += new String("</pre></body></html>");
            } catch (Exception ex) {
                sourceCode = "Could not load file: " + filename;
            }
	}
    }

    public String getName() {
	return getString(getResourceName() + ".name");
    }

    public Icon getIcon() {
	return createImageIcon(iconPath, getResourceName() + ".name");
    }

    public String getToolTip() {
	return getString(getResourceName() + ".tooltip");
    }

    public void mainImpl() {
	JFrame frame = new JFrame(getName());
        frame.getContentPane().setLayout(new BorderLayout());
	frame.getContentPane().add(getDemoPanel(), BorderLayout.CENTER);
	getDemoPanel().setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
	frame.pack();
	frame.setVisible(true);
    }

    public JPanel createHorizontalPanel(boolean threeD) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setAlignmentY(TOP_ALIGNMENT);
        p.setAlignmentX(LEFT_ALIGNMENT);
        if(threeD) {
            p.setBorder(loweredBorder);
        }
        return p;
    }
    
    public JPanel createVerticalPanel(boolean threeD) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentY(TOP_ALIGNMENT);
        p.setAlignmentX(LEFT_ALIGNMENT);
        if(threeD) {
            p.setBorder(loweredBorder);
        }
        return p;
    }

    public static void main(String[] args) {
	DemoModule demo = new DemoModule(null);
	demo.mainImpl();
    }

    public void init() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getDemoPanel(), BorderLayout.CENTER);
    }
    
    void updateDragEnabled(boolean dragEnabled) {}
}

