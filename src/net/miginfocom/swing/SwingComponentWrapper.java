/*
 * License (BSD):
 * ==============
 *
 * Copyright (c) 2004, Mikael Grev, MiG InfoCom AB. (miglayout (at) miginfocom (dot) com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * Neither the name of the MiG InfoCom AB nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 * @version 1.0
 * @author Mikael Grev, MiG InfoCom AB
 *         Date: 2006-sep-08
 */

package net.miginfocom.swing;

import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;

/** Swing component wrapper. */
public class SwingComponentWrapper implements ComponentWrapper {

    private static boolean maxSet = false;
    private static boolean vp = true;
    /** Debug color for component bounds outline. */
    private static final Color DB_COMP_OUTLINE = new Color(0, 0, 200);
    private final Component c;
    private int compType = TYPE_UNSET;

    public SwingComponentWrapper(Component c) {
        this.c = c;
    }

    public final int getBaseline(int width, int height) {
        if (BL_METHOD == null) {
            return -1;
        }
        try {
            Object[] args =
                    new Object[]{new Integer(c.getWidth()), new Integer(height)};
            return ((Integer) BL_METHOD.invoke(c, args)).intValue();
        } catch (Exception e) {
            return -1;
        }
    }

    public final Object getComponent() {
        return c;
    }
    /** Cache.
     */
    private static final IdentityHashMap<FontMetrics, Point.Float> FM_MAP =
            new IdentityHashMap<FontMetrics, Point.Float>(4);
    private static final Font SUBST_FONT =
            new Font("sansserif", Font.PLAIN, 11);

    public final float getPixelUnitFactor(boolean isHor) {
        Font font = c.getFont();
        FontMetrics fm = c.getFontMetrics(font != null ? font : SUBST_FONT);
        Point.Float p = FM_MAP.get(fm);
        if (p == null) {
            Rectangle2D r = fm.getStringBounds("X", c.getGraphics());
            p =     new Point.Float(((float) r.getWidth()) / 6f,
                    ((float) r.getHeight()) / 13.27734375f);
            FM_MAP.put(fm, p);
        }
        return isHor ? p.x : p.y;
    }
//	/** Cache.
//	 */
//	private final static IdentityHashMap<FontMetrics, Point.Float> FM_MAP2 = new IdentityHashMap<FontMetrics, Point.Float>(4);
//	private final static Font SUBST_FONT2 = new Font("sansserif", Font.PLAIN, 11);
//
//	public float getDialogUnit(boolean isHor)
//	{
//		Font font = c.getFont();
//		FontMetrics fm = c.getFontMetrics(font != null ? font : SUBST_FONT2);
//		Point.Float dluP = FM_MAP2.get(fm);
//		if (dluP == null) {
//			float w = fm.charWidth('X') / 4f;
//			int ascent = fm.getAscent();
//			float h = (ascent > 14 ? ascent : ascent + (15 - ascent) / 3) / 8f;
//
//			dluP = new Point.Float(w, h);
//			FM_MAP2.put(fm, dluP);
//		}
//		return isHor ? dluP.x : dluP.y;
//	}

    public final int getX() {
        return c.getX();
    }

    public final int getY() {
        return c.getY();
    }

    public final int getHeight() {
        return c.getHeight();
    }

    public final int getWidth() {
        return c.getWidth();
    }

    public final int getScreenLocationX() {
        Point p = new Point();
        SwingUtilities.convertPointToScreen(p, c);
        return p.x;
    }

    public final int getScreenLocationY() {
        Point p = new Point();
        SwingUtilities.convertPointToScreen(p, c);
        return p.y;
    }

    public final int getMinimumHeight() {
        c.getPreferredSize();
//		if (min == null) {
//			getPreferredHeight();   // To defeat a bug where the minimum size is difference before and after the first call to getPreferredSize();
//			min = ;
//		}
        return c.getMinimumSize().height;
    }

    public final int getMinimumWidth() {
        c.getPreferredSize();
//		if (min == null) {
//			getPreferredWidth();   // To defeat a bug where the minimum size is difference before and after the first call to getPreferredSize();
//			min = ;
//		}
        return c.getMinimumSize().width;
    }

    public final int getPreferredHeight() {
        return c.getPreferredSize().height;
    }

    public final int getPreferredWidth() {
        return c.getPreferredSize().width;
    }

    public final int getMaximumHeight() {
        if (!isMaxSet(c)) {
            return Short.MAX_VALUE;
        }
        return c.getMaximumSize().height;
    }

    public final int getMaximumWidth() {
        if (!isMaxSet(c)) {
            return Short.MAX_VALUE;
        }
        return c.getMaximumSize().width;
    }

    private boolean isMaxSet(Component c) {
        if (IMS_METHOD != null) {
            try {
                return (Boolean) IMS_METHOD.invoke(c, (Object) null);
            } catch (Exception e) {
                IMS_METHOD = null; // So we do not try every time.
            }
        }
        return isMaxSizeSetOn1_4();
    }

    public final ContainerWrapper getParent() {
        return new SwingContainerWrapper(c.getParent());
    }

    public final int getHorizontalScreenDPI() {
        return c.getToolkit().getScreenResolution();
    }

    public final int getVerticalScreenDPI() {
        return c.getToolkit().getScreenResolution();
    }

    public final int getScreenWidth() {
        return c.getToolkit().getScreenSize().width;
    }

    public final int getScreenHeight() {
        return c.getToolkit().getScreenSize().height;
    }

    public final boolean hasBaseline() {
        return getBaseline(10, 10) != -1;
    }

    public final String getLinkId() {
        return c.getName();
    }

    public final void setBounds(int x, int y, int width, int height) {
        c.setBounds(x, y, width, height);
    }

    public boolean isVisible() {
        return c.isVisible();
    }

    public final int[] getVisualPadding() {
        if (vp && c instanceof JTabbedPane) {
//			if (((JTabbedPane) c).getUI().getClass().getName().equals("com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI"))
            if (UIManager.getLookAndFeel().getClass().getName().
                    endsWith("WindowsLookAndFeel")) {
                return new int[]{-1, 0, 2, 2};
            }
        }
        return null;
    }

    public static boolean isMaxSizeSetOn1_4() {
        return maxSet;
    }

    public static void setMaxSizeSetOn1_4(boolean b) {
        maxSet = b;
    }

    public static final boolean isVisualPaddingEnabled() {
        return vp;
    }

    public static final void setVisualPaddingEnabled(boolean b) {
        vp = b;
    }

    public final void paintDebugOutline() {
        if (c.isShowing() == false) {
            return;
        }
        Graphics2D g = (Graphics2D) c.getGraphics();
        if (g == null) {
            return;
        }
        g.setPaint(DB_COMP_OUTLINE);
        g.setStroke(new BasicStroke(1f, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER, 10f, new float[]{2f, 4f}, 0));
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    public int getComponetType(boolean disregardScrollPane) {
        if (compType == TYPE_UNSET) {
            compType = checkType(disregardScrollPane);
        }
        return compType;
    }

    public int getLayoutHashCode() {
//		Dimension d = c.getMaximumSize();
//		int h = d.width + (d.height << 5);
//
//		d = c.getPreferredSize();
//		h += (d.width << 10) + (d.height << 15);
//
//		d = c.getMinimumSize();
//		h += (d.width << 20) + (d.height << 25);
//
//		if (c.isVisible())
//			h += 1324511;
//
//		String id = getLinkId();
//		if (id != null)
//			h += id.hashCode();
//		return h;
        // Since 2.3 will check the parent.isValid instead everything that affects that can be removed from the layout hashcode.
        String id = getLinkId();
        return id != null ? id.hashCode() : 1;
    }

    private final int checkType(boolean disregardScrollPane) {
        Component c = this.c;

        if (disregardScrollPane) {
            if (c instanceof JScrollPane) {
                c = ((JScrollPane) c).getViewport().getView();
            } else if (c instanceof ScrollPane) {
                c = ((ScrollPane) c).getComponent(0);
            }
        }

        if (c instanceof JTextField || c instanceof TextField) {
            return TYPE_TEXT_FIELD;
        } else if (c instanceof JLabel || c instanceof Label) {
            return TYPE_LABEL;
        } else if (c instanceof JToggleButton || c instanceof Checkbox) {
            return TYPE_CHECK_BOX;
        } else if (c instanceof AbstractButton || c instanceof Button) {
            return TYPE_BUTTON;
        } else if (c instanceof JComboBox || c instanceof Choice) {
            return TYPE_LABEL;
        } else if (c instanceof JTextComponent || c instanceof TextComponent) {
            return TYPE_TEXT_AREA;
        } else if (c instanceof JPanel || c instanceof Canvas) {
            return TYPE_PANEL;
        } else if (c instanceof JList || c instanceof List) {
            return TYPE_LIST;
        } else if (c instanceof JTable) {
            return TYPE_TABLE;
        } else if (c instanceof JSeparator) {
            return TYPE_SEPARATOR;
        } else if (c instanceof JSpinner) {
            return TYPE_SPINNER;
        } else if (c instanceof JProgressBar) {
            return TYPE_PROGRESS_BAR;
        } else if (c instanceof JSlider) {
            return TYPE_SLIDER;
        } else if (c instanceof JScrollPane) {
            return TYPE_SCROLL_PANE;
        } else if (c instanceof JScrollBar || c instanceof Scrollbar) {
            return TYPE_SCROLL_BAR;
        } else if (c instanceof Container) {
            // only AWT components is not containers.
            return TYPE_CONTAINER;
        }
        return TYPE_UNKNOWN;
    }

    public final int hashCode() {
        return getComponent().hashCode();
    }

    public final boolean equals(Object o) {
        if (o instanceof ComponentWrapper == false) {
            return false;
        }
        return getComponent().equals(((ComponentWrapper) o).getComponent());
    }
    /** Cached method used for getting base line with reflection.
     */
    private static Method BL_METHOD = null;
    static {
        try {
            BL_METHOD =
                    Component.class.getDeclaredMethod("getBaseline",
                    new Class[]{int.class, int.class});
        } catch (Throwable e) { // No such method or security exception
        }
    }
    private static Method IMS_METHOD = null;
    static {
        try {
            IMS_METHOD =
                    Component.class.getDeclaredMethod("isMaximumSizeSet",
                    (Class<?>) null);
        } catch (Throwable e) { // No such method or security exception
        }
    }
}