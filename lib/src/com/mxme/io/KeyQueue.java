package com.mxme.io;

import com.mxme.common.*;

public class KeyQueue
{
    private int [][]eventQueue;
    private int eventQueueTop;
    private int eventQueueBottom;
    
    private short m_size;

    /**
     * Creates a new event queue
     *
     * @param size Queue size
     */
    public KeyQueue(short size)
    {
        m_size = size;
        eventQueueTop = 0;
        eventQueueBottom = 0;
        eventQueue = new int[size][MXMEConstants.EVENT_QUEUE_FIELD_COUNT];
    }
    
    /**
     * Adds a key event to the queue
     *
     * @param key 
     * @param event
     *
     * @return
     */
    public synchronized boolean pushEvent(int key, int event)
    {
        if( eventQueueBottom == (eventQueueTop - 1) || (eventQueueBottom == m_size && eventQueueTop == 0) )
        {
            return false;
        } 
        else
        {
            eventQueue[eventQueueTop][MXMEConstants.EVENT_QUEUE_KEY_FIELD] = key;
            eventQueue[eventQueueTop][MXMEConstants.EVENT_QUEUE_TYPE_FIELD] = event;
            eventQueueTop--;

            if( eventQueueTop < 0 )
                eventQueueTop = m_size - 1;
            
            return true;
        }
    }
    
    /**
     * Gets a key event from the queue
     *
     * @return
     */
    public synchronized int[] popEvent()
    {
        if( eventQueueBottom == eventQueueTop )
        {
            return null;
        } 
        else
        {
        	int[] ret = eventQueue[eventQueueBottom--];

            if( eventQueueBottom < 0 )
                eventQueueBottom = m_size - 1;

            return ret;
        }
    }
    
    /**
     * Flushes the events in the queue
     *
     */
    public synchronized void clearEvents()
    {
        eventQueueBottom = 0;
        eventQueueTop = 0;
    }
    
    /**
     * Destroys the event queue
     */
    public void destroy()
    {
        eventQueue = null;
    }
    
}
