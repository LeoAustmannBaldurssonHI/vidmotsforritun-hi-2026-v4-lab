package hi.verkefni.vidmot.dataInterface;

/******************************************************************************
 *  Nafn    : Ebba Þóra Hvannberg
 *  T-póstur: ebba@hi.is
 *
 *  Lýsing  : Interface sem leyfir að setja gögn
 *  Útfært af contollerum sem vilja hlaða inn gögnum í
 *  viðmótshluti
 *
 *
 *****************************************************************************/

// Breytti bara nafn af interace frá gognInterace til DataInterface.
public interface DataInterface<T> {

    public void setGogn(T data);
}

