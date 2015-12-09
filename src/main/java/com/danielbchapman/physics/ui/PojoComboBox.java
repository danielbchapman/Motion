package com.danielbchapman.physics.ui;

import java.util.Collection;
import java.util.Vector;

import javax.swing.JComboBox;

@SuppressWarnings("rawtypes")
public class PojoComboBox<T> extends JComboBox
{
  private static final long serialVersionUID = 1L;

  @SuppressWarnings({ "unchecked" })
  public static <Q> Vector<Q> collectionToVector(Collection<Q> collection)
  {
    if (collection == null)
      return new Vector();

    Vector<Q> ret = new Vector<Q>();

    for (Q q : collection)
      ret.add(q);

    return ret;
  }

  public PojoComboBox()
  {
    super();
  }

  @SuppressWarnings("unchecked")
  public PojoComboBox(Collection<SelectItem<T>> items)
  {
    super(collectionToVector(items));
  }

  /**
   * Add a SelectItem&lt;T&gt; to the list
   * of possible items
   * @param item the item to add  
   * 
   */
  @SuppressWarnings("unchecked")
  public void addItem(SelectItem<T> item)
  {
    if (item == null)
      return;

    super.addItem(item);
  }

  /**
   * Add a select item to the list by specifying the name
   * and the value.
   * @param name the display name of the value
   * @param value the backing value for the element. 
   * 
   */
  public void addItem(String name, T value)
  {
    addItem(new SelectItem<T>(name, value));
  }

  public void addItems(Collection<SelectItem<T>> items)
  {
    if (items == null)
      return;

    for (SelectItem<T> s : items)
      addItem(s);
  }

  /**
   * Ask this ComboBox to check its model and then return 
   * true if a backing selectItem already is mapped to the values.
   * @param t
   * @return <Return Description>  
   * 
   */
  public boolean contains(T t)
  {
    if (t == null)
      return false;

    SelectItem<T>[] values = getItems();
    for (int i = 0; i < values.length; i++)
      if (t.equals(values[i].getValue()))
        return true;

    return false;
  }

  /**
   * Return a list of all SelectItems that
   * are present for this model.
   * 
   * @return the SelectItem[] showing the backing model.  
   */
  @SuppressWarnings("unchecked")
  public SelectItem<T>[] getItems()
  {
    int size = getModel().getSize();
    SelectItem[] values = new SelectItem[size];

    for (int i = 0; i < size; i++)
      values[i] = (SelectItem) getModel().getElementAt(i);

    return values;
  }


  @SuppressWarnings("unchecked")
  public String getSelectedName()
  {
    SelectItem<T> item = (SelectItem<T>) getSelectedItem();
    return item.getName();
  }
  
  @SuppressWarnings("unchecked")
  public T getSelectedValue()
  {
    SelectItem<T> item = (SelectItem<T>) getSelectedItem();
    T ret = item.getValue();
    return ret;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setSelectedItem(Object value)
  {
    if (value == null)
      return;
    // if(value == null)
    // {
    // super.setSelectedItem(null);
    // return;
    // }

    int count = getItemCount();
    for (int i = 0; i < count; i++)
    {
      SelectItem<T> t = (SelectItem<T>) getItemAt(i);
      if (t == null)
        continue;
      if (t.getValue() == null ? false : (t.getValue().equals(value) || t.equals(value)))
      {
        /* Can't set by index--won't work */
        super.setSelectedItem(super.getItemAt(i));
        return;
      }
    }
  }
}
