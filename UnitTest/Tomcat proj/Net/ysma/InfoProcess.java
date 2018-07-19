
package Net.ysma;

public abstract class InfoProcess {
    protected SerialObj _seoObjSerie;

    public InfoProcess(SerialObj obj) {
        _seoObjSerie = obj;
    }

    public abstract SerialObj Aiguilleur(SerialObj temp);
}
