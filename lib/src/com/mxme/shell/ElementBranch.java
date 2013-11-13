package com.mxme.shell;

/**************************
 * ElementBranch class
 * 
 *      Used to create a Tree of Elements
 * 
 *  Notes:
 *      
 *     
 */
import com.mxme.shell.core.Menu;

public class ElementBranch
{
    public static final boolean REMOVE_REDUNDANT_SOFT_BUTTONS = true;
    public static final byte ARRAY_CAPACITY = 12;
    
    public Element element;
    public final ElementBranch [] branch = new ElementBranch[ARRAY_CAPACITY];
    public ElementBranch parent;
    public int branchCount = 0;

    /**
     * Adds a branch to the tree
     *
     * @param p_leaf
     * @return
     */
    public ElementBranch addBranch(Element p_leaf)
    {
        // Look for the next open branch
        int new_index = 0;
        while( branch[new_index] != null )
            ++new_index;
        
        // create the branch
        branch[new_index] = new ElementBranch();
        
        // Remove redundant soft buttons if necessary
        if( REMOVE_REDUNDANT_SOFT_BUTTONS )
            // We also state that this element now NEEDS the Ok button
            element.m_nRightSoftButton = Shell.m_nIDOk;
        
        ++branchCount;
        return initBranch(branch[new_index], this, p_leaf);
    }

    public ElementBranch addBranchVirgin(Element p_leaf)
    {
        return addBranch(p_leaf).parent;
    }

    public ElementBranch addBranchWithChilds(Element p_leaf)
    {
        return addBranch(p_leaf);
    }

    public ElementBranch getParent()
    {
        return parent;
    }

    /**
     * Initializes a branch, settings its parent and its Element.
     *
     * @param branch
     * @param parent
     * @param p_leaf
     * @return
     */
    private static ElementBranch initBranch(ElementBranch branch, ElementBranch parent, Element p_leaf)
    {
        branch.parent = parent;
        branch.element = p_leaf;
        
        if( REMOVE_REDUNDANT_SOFT_BUTTONS )
            // Since at the time being there're no sub-branches, our current 
            // element  doesn't need a redundant "Ok" button, just the "Back" one.
            branch.element.m_nRightSoftButton = Shell.m_nHidden;
        
        //  Special addendum, if our element is going to  be a Menu, then we 
        // can just add this Branch as its menu automatically
        if( branch.element instanceof Menu )
            ((Menu)branch.element).setMenu(branch);
        
        return branch;
    }

    /**
     * Creates a new Tree Root with the given Element inside and
     * returns it.
     *
     * @param p_rootElement
     * @return
     */
    public static ElementBranch createRoot(Element p_rootElement)
    {
        // Since every root is an orphan, we just create one and return it
        return createOrphan(p_rootElement);
    }

    /**
     *
     * @param p_orphanElement
     * @return
     */
    public static ElementBranch createOrphan(Element p_orphanElement)
    {
        // An orphan is a Branch without parent, we just
        // create one and return it.
        return initBranch(new ElementBranch(), null, p_orphanElement);
    }
    
    /**
     * Removes all branches but preserves this Branch integrity by
     * leaving its parent and its Element intact
     * 
     */
    public void removeAllBranches()
    {
        for( int i = 0; i < branch.length; i++ )
            branch[i] = null;
        branchCount = 0;
    }
    
}
