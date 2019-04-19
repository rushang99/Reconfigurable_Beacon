package com.example.rohit.driverapp;

class Node<B_data>
{
    public B_data data;
    public Node<B_data> next;

    public Node(B_data s, Node<B_data> n)
    {
        data=s;
        next=n;
    }
    public B_data getData()
    {
        return data;
    }
    public Node<B_data> getNext()
    {
        return next;
    }
    public void setData(B_data data1)
    {
        data=data1;
    }
    public void setNext(Node<B_data> b)
    {
        next=b;
    }
}

public class LinkedList<T>
{
    public Node<B_data> head;
    public int size;

    public LinkedList()
    {
        size=0;
    }
    public void addFirst(Node<B_data> v)
    {
        v.setNext(head);
        head=v;
        size+=1;
    }
    public void addLast(Node<B_data> t)
    {
        if(head==null)
        {
            addFirst(t);
        }
        else
        {
            Node<B_data> r=head;
            while(r.getNext()!=null)
            {
                r=r.getNext();
            }
            r.setNext(t);
        }
        size+=1;

    }
    public void remove(Node<B_data> d)
    {
        try
        {
            Node<B_data> r=head;
            if(r==d)
            {
                head=head.getNext();
            }
            else
            {
                while(r.getNext()!=d)
                {
                    r=r.getNext();
                }
                r.setNext(d.getNext());
            }
            size=size-1;
        }
        catch(NullPointerException e)
        {
            System.out.print("Node<T> not found");
        }

    }
    public Boolean IsMember(B_data o)
    {
        Boolean d=false;
        if(head==null)
        {
            d=false;
        }
        else
        {

            Node<B_data> r=head;
            while(r.getNext()!=null)
            {
                if(r.data.B_uuid.equals(o.B_uuid))
                {
                    d=true;
                }
                r=r.getNext();
            }
            if(r.data.B_uuid.equals(o.B_uuid))
            {
                d=true;
            }


        }
        return d;

    }

}

