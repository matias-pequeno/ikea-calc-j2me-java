/****
 * Gfx Class
 *
 * This class contains, loads and unloads all graphics used during
 * the whole application.
 * It also uses "packages", which is a collection of IDs of images and sprites
 * that will be loaded and unloaded as a group.
 *
 */
package util;

// Lib imports
import com.mxme.lcdui.GfxManager;
import com.mxme.lcdui.ImageContainer;

public class Gfx extends GfxManager
{
    /**
     * Full image listing
     *
     */
    public static ImageContainer
            // Intro screen
            imgLogo                 = new ImageContainer("logo"),
            // Main Menu screen
            imgMenuBackSubtitle     = new ImageContainer("t01"),
            // Standard Menus
            imgStdMenuLine          = new ImageContainer("line"),
            imgStdMenuScroll        = new ImageContainer("scroll"),
            // Calculator generic screen
            imgCalculatorSubtitle   = new ImageContainer("t11"),
            imgCalculatorButton     = new ImageContainer("butsm"),
            imgCalculatorButtonSel  = new ImageContainer("butsms"),
            imgCalculatorButtonDoneText = new ImageContainer("txtd"),
            imgCalculatorButtonHelpText = new ImageContainer("txth"),
            imgCalculatorButtonText     = new ImageContainer("txtc"),
            imgCalculatorSmallTextBox   = new ImageContainer("tb00"),
            imgCalculatorTextBox    = new ImageContainer("tb10"),
            imgCalculatorAncho      = new ImageContainer("anchcm"),
            imgCalculatorAlto       = new ImageContainer("altcm"),
            // Terminos y Condiciones
            imgTermsSubtitle        = new ImageContainer("t81"),
            // Standard
            imgStdTitle             = new ImageContainer("t00"),
            imgStdTermsText         = new ImageContainer("term"),
            imgStdCmdOk             = new ImageContainer("cmda"),
            imgStdCmdBack           = new ImageContainer("cmdb"),
            imgStdCmdBackLeft       = new ImageContainer("cmdvv"),
            imgStdCmdExit           = new ImageContainer("cmde"),
            imgStdCmdClear          = new ImageContainer("cmdc"),
            imgStdCmdMenu           = new ImageContainer("cmdm"),
            imgStdCmdMore           = new ImageContainer("cmdv");

    /**
     * Graphic packages
     * 
     */
    //  [0][] = Images, [1][] = Sprites
    public static ImageContainer[][] pkgStd    = { {imgStdTitle, imgStdTermsText, imgStdCmdOk, imgStdCmdBack, imgStdCmdBackLeft, imgStdCmdExit, imgStdCmdClear, imgStdCmdMenu, imgStdCmdMore}, {} };
    public static ImageContainer[][] pkgMenu   = { {imgMenuBackSubtitle}, {} };
    public static ImageContainer[][] pkgStdMenu= { {imgStdMenuLine, imgStdMenuScroll}, {} };
    public static ImageContainer[][] pkgDemoForm = { {imgCalculatorSubtitle, imgCalculatorButton, imgCalculatorButtonSel, imgCalculatorButtonText, imgCalculatorButtonDoneText, imgCalculatorButtonHelpText, imgCalculatorTextBox, imgCalculatorSmallTextBox, imgCalculatorAncho, imgCalculatorAlto}, {} };

    public static ImageContainer[][] pkgViewer = { {}, {} };
    public static ImageContainer[][] pkgViewerTerms = { {imgTermsSubtitle}, {} };
    
}
