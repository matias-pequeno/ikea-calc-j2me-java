package com.mxme.shell;

/**************************
 * IStandardLayout interface
 * 
 *  Interface that defines a Standard Layout to be used on a Shell and that
 * will be overriden whenever you need to draw a special Shell layout
 * 
 *  Notes:
 *      
 *     
 */
import javax.microedition.lcdui.Graphics;

public interface IStandardLayout 
{
    /****
     * Draw standard layout background.<br>
     *  <br>
     * This method draws the background using the standard layout.
     * 
     * @param g, Graphics object
     */
    abstract void draw_background(Graphics g, Element e);
    
    /****
     * Draw standard layout title.<br>
     *  <br>
     * This method draws the title using the standard layout.
     * 
     * @param g, Graphics object
     */
    abstract void draw_title(Graphics g, Element e);
    
    /****
     * Draw standard layout soft buttons.<br>
     *  <br>
     * This method draws the soft buttons using the standard layout.
     * 
     * @param g, Graphics object
     */
    abstract void draw_command_buttons(Graphics g, Element e);
    
    /****
     * Draw standard layout.<br>
     *  <br>
     * This method draws a standard layout.
     * 
     * @param g, Graphics object
     */
    abstract void draw_standard_layout(Graphics g, Element e);
    
}
