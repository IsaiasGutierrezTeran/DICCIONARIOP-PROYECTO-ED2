package negocio;

//import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class ArbolBinarioBusqueda< K extends Comparable<K>, V>
        implements IArbolBinario<K, V> {

    public ArbolBinarioBusqueda() {

    }
    protected NodoBinario<K, V> raiz;
    private static final String UBICACION_ARCHIVO = "src\\Datos\\Diccionario.txt";

    @Override
    public void cargarDatosDelTxt() {
        try {

            BufferedReader lector = new BufferedReader(new FileReader(UBICACION_ARCHIVO));

            String linea;
            int i = 0;
            while ((linea = lector.readLine()) != null) {
                String[] palabras = linea.split("-");
                K clave = (K) palabras[0].trim();
                V valor = (V) palabras[1].trim();
                this.insertar(clave, valor);
            }

            lector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArbolBinarioBusqueda(List<K> clavesInOrden, List<V> valoresInOrden,
            List<K> clavesNoInorden, List<V> valoresNoInorden,
            boolean usandoPreOrnden) {
        if (clavesInOrden == null
                || clavesNoInorden == null
                || valoresInOrden == null
                || valoresNoInorden == null) {
            throw new IllegalArgumentException("Los parametos no puden ser nulos");

        }
        if (clavesInOrden.isEmpty()
                || clavesNoInorden.isEmpty()
                || valoresInOrden.isEmpty()
                || valoresNoInorden.isEmpty()) {
            throw new IllegalArgumentException("Los parametros no pueden ser Nulos");
        }
        if (clavesInOrden.size() != clavesNoInorden.size()
                || clavesInOrden.size() != valoresInOrden.size()
                || clavesInOrden.size() != valoresNoInorden.size()) {
            throw new IllegalArgumentException("Los parametros no pueden ser Listas con diferentes tamanio");
        }

        if (usandoPreOrnden) {
            this.raiz = recontruirConPreOrden(clavesInOrden, valoresInOrden,
                    clavesNoInorden, valoresNoInorden);
        } else {
            recontruirConPostOrden(clavesInOrden, valoresInOrden,
                    clavesNoInorden, valoresNoInorden);
        }

    }

    private NodoBinario<K, V> recontruirConPreOrden(List<K> clavesInOrden, List<V> valoresInOrden,
            List<K> clavesPreorden, List<V> valoresPreorden) {
        if (clavesInOrden.isEmpty()) {
            return (NodoBinario<K, V>) NodoBinario.nodoVacio();
        }
        int posicionPadrePreOrden = 0;
        K clavePadrePreOrden = clavesPreorden.get(posicionPadrePreOrden);
        V valorPadrePreOrden = valoresPreorden.get(posicionPadrePreOrden);
        int posisionPadreInOrden = posicionDeClave(clavePadrePreOrden, clavesInOrden);

        //------------ analizamos por izquierda -------------------------------
        List<K> listaClavesInOrdenIZquierda = clavesInOrden.subList(0, posisionPadreInOrden);
        List<V> listaValoresInOrdenIZquierda = valoresInOrden.subList(0, posisionPadreInOrden);
        List<K> listaClavesPreOrdenIZquierda = clavesPreorden.subList(1, 1 + posisionPadreInOrden);
        List<V> listaValoresPreOrdenIZquierda = valoresPreorden.subList(1, 1 + posisionPadreInOrden);

        NodoBinario<K, V> nodoIzquierdo = recontruirConPreOrden(listaClavesInOrdenIZquierda, listaValoresInOrdenIZquierda,
                listaClavesPreOrdenIZquierda, listaValoresPreOrdenIZquierda);
        //------------ analizamos por derecha ---------------------------------
        List<K> listaClavesInOrdenDerecha = clavesInOrden.subList(posisionPadreInOrden + 1, clavesInOrden.size());
        List<V> listaValoresInOrdenDerecha = valoresInOrden.subList(posisionPadreInOrden + 1, valoresInOrden.size());
        List<K> listaClavesPreOrdenDerecha = clavesPreorden.subList(1 + posisionPadreInOrden, clavesPreorden.size());
        List<V> listaValoresPreOrdenDerecha = valoresPreorden.subList(1 + posisionPadreInOrden, valoresPreorden.size());
        NodoBinario<K, V> nodoDerecho = recontruirConPreOrden(listaClavesInOrdenDerecha, listaValoresInOrdenDerecha,
                listaClavesPreOrdenDerecha, listaValoresPreOrdenDerecha);

        //------------ nodo Actual -------------------------------------------
        NodoBinario<K, V> nodoActual = new NodoBinario<>(clavePadrePreOrden, valorPadrePreOrden);
        nodoActual.setHijoIzquierdo(nodoIzquierdo);
        nodoActual.setHijoDerecho(nodoDerecho);

        return nodoActual;
    }

    private NodoBinario<K, V> recontruirConPostOrden(List<K> clavesInOrden, List<V> valoresInOrden,
            List<K> clavesPostOrden, List<V> valoresPostOrden) {
        if (clavesInOrden.isEmpty()) {
            return (NodoBinario<K, V>) NodoBinario.nodoVacio();

        }
        int posicionPadrePostOrden = clavesPostOrden.size() - 1;
        K clavePadrePosOrden = clavesPostOrden.get(posicionPadrePostOrden);
        V valorPadrePosOrden = valoresPostOrden.get(posicionPadrePostOrden);
        int posicionPadreInOrden = posicionDeClave(clavePadrePosOrden, clavesInOrden);
        //------------ analizamos por izquierda -------------------------------
        List<K> listaClavesInOrdenIZquierda = clavesInOrden.subList(0, posicionPadreInOrden);
        List<V> listaValoresInOrdenIZquierda = valoresInOrden.subList(0, posicionPadreInOrden);
        List<K> listaClavesPostOrdenIZquierda = clavesPostOrden.subList(0, posicionPadreInOrden);
        List<V> listaValoresPostOrdenIZquierda = valoresPostOrden.subList(0, posicionPadreInOrden);

        NodoBinario<K, V> nodoIzquierdo = recontruirConPostOrden(listaClavesInOrdenIZquierda, listaValoresInOrdenIZquierda,
                listaClavesPostOrdenIZquierda, listaValoresPostOrdenIZquierda);
        //------------ analizamos por derecha ---------------------------------
        List<K> listaClavesInOrdenDerecha = clavesInOrden.subList(1 + posicionPadreInOrden, clavesInOrden.size());
        List<V> listaValoresInOrdenDerecha = valoresInOrden.subList(1 + posicionPadreInOrden, valoresInOrden.size());
        List<K> listaClavesPostOrdenDerecha = clavesPostOrden.subList(posicionPadreInOrden, clavesPostOrden.size() - 1);
        List<V> listaValoresPostOrdenDerecha = valoresPostOrden.subList(posicionPadreInOrden, clavesPostOrden.size() - 1);
        NodoBinario<K, V> nodoDerecho = recontruirConPostOrden(listaClavesInOrdenDerecha, listaValoresInOrdenDerecha,
                listaClavesPostOrdenDerecha, listaValoresPostOrdenDerecha);

        //------------ nodo Actual -------------------------------------------
        NodoBinario<K, V> nodoActual = new NodoBinario<>(clavePadrePosOrden, valorPadrePosOrden);
        nodoActual.setHijoIzquierdo(nodoIzquierdo);
        nodoActual.setHijoDerecho(nodoDerecho);

        return nodoActual;

    }

    private int posicionDeClave(K claveBuscar, List<K> listaClaves) {
        for (int i = 0; i < listaClaves.size(); i++) {
            K claveActual = listaClaves.get(i);
            if (claveActual.compareTo(claveBuscar) == 0) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void vaciar() {
        this.raiz = null;
    }

    @Override
    public boolean esArbolVacio() {
        return NodoBinario.esNodoVacio(raiz);
    }

    @Override
    public int size() {
        Stack<NodoBinario<K, V>> pilaNodos = new Stack<>();
        int cantidad = 0;

        pilaNodos.push(this.raiz);
        while (!pilaNodos.isEmpty()) {
            NodoBinario<K, V> nodoActual = pilaNodos.pop();
            cantidad++;
            if (!nodoActual.esHijoDerechoVacio()) {
                pilaNodos.push(nodoActual.getHijoDerecho());
            }
            if (!nodoActual.esHijoIzquierdoVacio()) {
                pilaNodos.push(nodoActual.getHijoIzquierdo());
            }
        }
        return cantidad;
    }

    @Override
    public int altura() {
        return altura(raiz);
    }

    protected int altura(NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int alturaPorLaIzquierda = altura(nodoActual.getHijoIzquierdo());
        int alturaPorLaDerecha = altura(nodoActual.getHijoIzquierdo());
        return alturaPorLaIzquierda > alturaPorLaDerecha ? alturaPorLaIzquierda + 1
                : alturaPorLaDerecha + 1;
    }

    public int alturaIT() {
        if (this.esArbolVacio()) {
            return 0;
        }
        int altura = 0;
        Queue<NodoBinario<K, V>> colaNodos = new LinkedList<>();
        colaNodos.offer(raiz);

        while (!colaNodos.isEmpty()) {
            int cantidadDeNodo = colaNodos.size();
            int i = 0;
            while (i < cantidadDeNodo) {
                NodoBinario<K, V> nodoActual = colaNodos.poll();
                if (!nodoActual.esHijoIzquierdoVacio()) {
                    colaNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esHijoDerechoVacio()) {
                    colaNodos.offer(nodoActual.getHijoDerecho());
                }
                i++;
            }
            altura++;
        }
        return altura;
    }

    @Override
    public int nivel() {
        if (this.esArbolVacio()) {
            return 0;
        }
        int nivel = -1;
        Queue<NodoBinario<K, V>> colaNodos = new LinkedList<>();
        colaNodos.offer(raiz);

        while (!colaNodos.isEmpty()) {
            int cantidadDeNodo = colaNodos.size();
            int i = 0;
            while (i < cantidadDeNodo) {
                NodoBinario<K, V> nodoActual = colaNodos.poll();
                if (!nodoActual.esHijoIzquierdoVacio()) {
                    colaNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esHijoDerechoVacio()) {
                    colaNodos.offer(nodoActual.getHijoDerecho());
                }
                i++;
            }
            nivel++;
        }
        return nivel;
    }

    @Override
    public K minimo() {
        NodoBinario<K, V> nodoActual = this.raiz;
        NodoBinario<K, V> nodoAnterior = nodoActual;
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        return nodoAnterior.getClave();
    }

    @Override
    public K maximo() {
        NodoBinario<K, V> nodoActual = this.raiz;
        NodoBinario<K, V> nodoAnterior = nodoActual;
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijoDerecho();
        }
        return nodoAnterior.getClave();
    }

    @Override
    public void insertar(K clave, V valor) {
        if (clave == null) {
            throw new IllegalArgumentException("Clave no puede ser nula");
        }
        if (valor == null) {
            throw new IllegalArgumentException("Valor no puede ser nula");
        }
        if (this.esArbolVacio()) {
            this.raiz = new NodoBinario<>(clave, valor);
            return;
        }
        NodoBinario<K, V> nodoActual = this.raiz;
        NodoBinario<K, V> nodoAnterior = nodoActual;
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            K claveActual = nodoActual.getClave();
            nodoAnterior = nodoActual;
            if (claveActual.compareTo(clave) > 0) {
                nodoActual = nodoActual.getHijoIzquierdo();
            } else if (claveActual.compareTo(clave) < 0) {
                nodoActual = nodoActual.getHijoDerecho();
            } else { // si la clave existe remplazamos
                nodoActual.setValor(valor);
                return;
            }
        }
        // si llego quiere decir que hay que insertar
        NodoBinario<K, V> nuevoNodo = new NodoBinario<>(clave, valor);
        if (nodoAnterior.getClave().compareTo(clave) > 0) {
            nodoAnterior.setHijoIzquierdo(nuevoNodo);
        } else {
            nodoAnterior.setHijoDerecho(nuevoNodo);
        }
    }

    @Override
    public void eliminar(K clave) {
        if (clave == null) {
            throw new IllegalArgumentException("Clave a eliminar no puede ser nula");
        }
        this.raiz = eliminar(this.raiz, clave);
    }

    private NodoBinario<K, V> eliminar(NodoBinario<K, V> nodo, K clave) {
        if (NodoBinario.esNodoVacio(nodo)) {
            return nodo;
        }

        if (clave.compareTo(nodo.getClave()) < 0) {
            nodo.setHijoIzquierdo(eliminar(nodo.getHijoIzquierdo(), clave));
        } else if (clave.compareTo(nodo.getClave()) > 0) {
            nodo.setHijoDerecho(eliminar(nodo.getHijoDerecho(), clave));
        } else {
            if (nodo.esHijoIzquierdoVacio()) {
                return nodo.getHijoDerecho();
            } else if (nodo.esHijoDerechoVacio()) {
                return nodo.getHijoIzquierdo();
            }

            // Encuentra el nodo sucesor en orden (el menor valor en el subárbol derecho)
            NodoBinario<K, V> sucesor = Sucesor(nodo.getHijoDerecho());

            // Copia los datos del sucesor al nodo actual
            nodo.setClave(sucesor.getClave());
            nodo.setValor(sucesor.getValor());

            // Elimina el sucesor
            nodo.setHijoDerecho(eliminar(nodo.getHijoDerecho(), sucesor.getClave()));
        }

        return nodo;
    }

    protected NodoBinario<K, V> Sucesor(NodoBinario<K, V> nodo) {
        NodoBinario<K, V> actual = nodo;
        while (!actual.esHijoIzquierdoVacio()) {
            actual = actual.getHijoIzquierdo();
        }
        return actual;
    }

    public void eliminarI(K clave) {
        NodoBinario<K, V> nodoActual = this.raiz;
        NodoBinario<K, V> nodoPadre = null;
        NodoBinario<K, V> nodoSucesor = null;
        NodoBinario<K, V> nodoPadreSucesor = null;
        boolean hallado = false;
        while (!hallado) { // buscamos el nodo a eliminar
            if (!NodoBinario.esNodoVacio(nodoActual)) {
                if (clave.compareTo(nodoActual.getClave()) == 0) { // si lo encontramos
                    hallado = true;
                } else {

                    if (clave.compareTo(nodoActual.getClave()) > 0) {
                        nodoPadre = nodoActual;
                        nodoActual = nodoActual.getHijoDerecho();
                    } else {
                        nodoPadre = nodoActual;
                        nodoActual = nodoActual.getHijoIzquierdo();
                    }
                }
            } else {
                hallado = true;
            }

        }
        //-- si llegamos aca econtarmos algo analizamos lo que encontramos
        if (!NodoBinario.esNodoVacio(nodoActual)) {

            //-------estamos dentro del arbol -----------------------------
            //----- 1 caso si es hoja -------------------------------------
            if (nodoActual.esHoja()) {

                //------ en el caso que sea un nodo hoja --------------------
                if (nodoActual.getClave().compareTo(nodoPadre.getClave()) < 0) {
                    nodoPadre.setHijoIzquierdo(null);
                } else {
                    nodoPadre.setHijoDerecho(null);
                }

            } else {
                //---------2 caso si el nodo tiene almenos un hijo ----------------
                if (!nodoActual.esHijoDerechoVacio() && nodoActual.esHijoIzquierdoVacio()) {
                    //------- nodo actual esta a la derecha----------
                    if (nodoActual.getClave().compareTo(nodoPadre.getClave()) > 0) {
                        nodoPadre.setHijoDerecho(nodoActual.getHijoDerecho());
                    } else {
                        //------- nodo actual esta a la izquierda----------
                        nodoPadre.setHijoIzquierdo(nodoActual.getHijoDerecho());
                    }
                } else if (nodoActual.esHijoDerechoVacio() && !nodoActual.esHijoIzquierdoVacio()) {
                    //------- nodo actual esta a la derecha----------
                    if (nodoActual.getClave().compareTo(nodoPadre.getClave()) > 0) {
                        nodoPadre.setHijoDerecho(nodoActual.getHijoIzquierdo());
                    } else {
                        //------- nodo actual esta a la izquierda----------
                        nodoPadre.setHijoIzquierdo(nodoActual.getHijoIzquierdo());
                    }
                } else {
                    //--------- 3 caso el nodo tiene los hijos completo --------------  
                    nodoSucesor = nodoActual.getHijoDerecho();
                    nodoPadreSucesor = nodoActual;
                    while (!nodoSucesor.esHijoIzquierdoVacio()) { // buscamos el sucesor 
                        nodoPadreSucesor = nodoSucesor;
                        nodoSucesor = nodoSucesor.getHijoIzquierdo();
                    }
                    //----- analizamos los casos del sucesor---------------------
                    if (nodoSucesor.esHoja()) {
                        //-------- si el sucesor es hoja--------------------
                        if (nodoPadreSucesor != nodoActual) {
                            //------ el sucesor tiene padre ----------------
                            nodoPadreSucesor.setHijoIzquierdo(null);
                            nodoSucesor.setHijoDerecho(nodoActual.getHijoDerecho());
                            nodoSucesor.setHijoIzquierdo(nodoActual.getHijoIzquierdo());

                        } else {
                            //------ si el sucesor no tiene padre-----------
                            nodoSucesor.setHijoIzquierdo(nodoActual.getHijoIzquierdo());
                        }
                        nodoActual.setHijoDerecho(null);
                        nodoActual.setHijoIzquierdo(null);
                    } else {
                        //---- el sucesor tiene hijos ----------------------
                        if (nodoPadreSucesor != nodoActual) {
                            //----el sucesor tiene padre--------------------
                            nodoPadreSucesor.setHijoIzquierdo(nodoSucesor.getHijoDerecho());
                            nodoSucesor.setHijoDerecho(nodoActual.getHijoDerecho());
                            nodoSucesor.setHijoIzquierdo(nodoActual.getHijoIzquierdo());
                        } else {
                            //---- el sucesor no tiene padre----------------
                            nodoSucesor.setHijoIzquierdo(nodoActual.getHijoIzquierdo());
                        }
                        nodoActual.setHijoDerecho(null);
                        nodoActual.setHijoIzquierdo(null);
                    }
                    if (NodoBinario.esNodoVacio(nodoPadre)) {
                        //---------estamos en la raiz--------------
                        this.raiz.setClave(nodoSucesor.getClave());
                        this.raiz.setValor(nodoSucesor.getValor());
                        this.raiz.setHijoDerecho(nodoSucesor.getHijoDerecho());
                        this.raiz.setHijoIzquierdo(nodoSucesor.getHijoIzquierdo());

                    } else {
                        if (nodoSucesor.getClave().compareTo(nodoPadre.getClave()) > 0) {
                            nodoPadre.setHijoDerecho(nodoSucesor);

                        } else {
                            nodoPadre.setHijoIzquierdo(nodoSucesor);
                        }
                    }

                }

            }

        }

    }

    @Override
    public boolean contiene(K clave) {
        return this.buscar(clave) != null;
    }

    @Override
    public V buscar(K clave
    ) {

        Stack<NodoBinario<K, V>> pilaAuxiliar = new Stack<>();
        pilaAuxiliar.add(raiz);
        while (!pilaAuxiliar.isEmpty()) {
            NodoBinario<K, V> nodoActual = pilaAuxiliar.pop();
            if (nodoActual.getClave().compareTo(clave) == 0) {
                return nodoActual.getValor();
            }
            if (!nodoActual.esHijoDerechoVacio()) {
                pilaAuxiliar.push(nodoActual.getHijoDerecho());
            }
            if (!nodoActual.esHijoIzquierdoVacio()) {
                pilaAuxiliar.push(nodoActual.getHijoIzquierdo());
            }

        }
        return null;
    }

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> listaElementos = new ArrayList<>();
        if (this.esArbolVacio()) {
            return listaElementos;
        }
        Stack<NodoBinario<K, V>> pilaAuxiliar = new Stack<>();
        llenarPilaInOrden(pilaAuxiliar, raiz);
        while (!pilaAuxiliar.isEmpty()) {
            NodoBinario<K, V> nodoActual = pilaAuxiliar.peek();
            if (!nodoActual.esHijoDerechoVacio()) {
                nodoActual = pilaAuxiliar.pop();
                listaElementos.add(nodoActual.getClave());
                llenarPilaInOrden(pilaAuxiliar, nodoActual.getHijoDerecho());
            } else {
                nodoActual = pilaAuxiliar.pop();
                listaElementos.add(nodoActual.getClave());
            }
        }
        return listaElementos;
    }

    private void llenarPilaInOrden(Stack<NodoBinario<K, V>> pilaAuxiliar, NodoBinario<K, V> nodo) {

        while (!NodoBinario.esNodoVacio(nodo)) {
            pilaAuxiliar.push(nodo);
            nodo = nodo.getHijoIzquierdo();
        }

    }

    @Override
    public List<K> recorridoEnPosOrden() {
        List<K> listaElementos = new ArrayList<>();
        if (this.esArbolVacio()) {
            return listaElementos;
        }
        Stack<NodoBinario<K, V>> pilaAuxiliar = new Stack<>();
        NodoBinario<K, V> nodoActual = this.raiz;
        llenarPilaPostOrden(pilaAuxiliar, nodoActual);
        while (!pilaAuxiliar.isEmpty()) {
            nodoActual = pilaAuxiliar.pop();
            listaElementos.add(nodoActual.getClave());
            if (!pilaAuxiliar.isEmpty()) {
                NodoBinario<K, V> nodoTope = pilaAuxiliar.peek();
                if (!nodoTope.esHijoDerechoVacio() && nodoTope.getHijoDerecho() != nodoActual) {
                    llenarPilaPostOrden(pilaAuxiliar, nodoTope.getHijoDerecho());
                }
            }
        }
        return listaElementos;
    }

    private void llenarPilaPostOrden(Stack<NodoBinario<K, V>> pilaAuxiliar, NodoBinario<K, V> nodo) {
        while (!NodoBinario.esNodoVacio(nodo)) {
            pilaAuxiliar.push(nodo);
            if (!nodo.esHijoIzquierdoVacio()) {
                nodo = nodo.getHijoIzquierdo();
            } else {
                nodo = nodo.getHijoDerecho();
            }

        }
    }

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> listaElementos = new ArrayList<>();
        if (this.esArbolVacio()) {
            return listaElementos;
        }
        Stack<NodoBinario<K, V>> pilaAuxiliar = new Stack<>();
        pilaAuxiliar.add(raiz);
        while (!pilaAuxiliar.isEmpty()) {
            NodoBinario<K, V> nodoActual = pilaAuxiliar.pop();
            listaElementos.add(nodoActual.getClave());
            if (!nodoActual.esHijoDerechoVacio()) {
                pilaAuxiliar.push(nodoActual.getHijoDerecho());
            }
            if (!nodoActual.esHijoIzquierdoVacio()) {
                pilaAuxiliar.push(nodoActual.getHijoIzquierdo());
            }

        }
        return listaElementos;
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> listaElementos = new ArrayList<>();
        if (this.esArbolVacio()) {
            return listaElementos;
        }
        Queue<NodoBinario<K, V>> colaNodos = new LinkedList<>();
        colaNodos.add(raiz);
        while (!colaNodos.isEmpty()) {
            NodoBinario<K, V> nodoActual = colaNodos.poll();

            listaElementos.add(nodoActual.getClave());
            if (!nodoActual.esHijoIzquierdoVacio()) {
                colaNodos.offer(nodoActual.getHijoIzquierdo());
            }
            if (!nodoActual.esHijoDerechoVacio()) {
                colaNodos.offer(nodoActual.getHijoDerecho());
            }

        }
        return listaElementos;
    }

    public void imprimirArbol() {
        imprimirArbol(raiz, "", false, null);
    }

    private void imprimirArbol(NodoBinario<K, V> nodo, String prefijo, boolean esHijoIzquierdo, NodoBinario<K, V> nodoPadre) {
        if (nodo != null) {
            String simboloRama = esHijoIzquierdo ? "├── " : "└── ";
            System.out.println(prefijo + simboloRama + nodo.getClave() + " [" + (nodoPadre != null && nodo.getClave().compareTo(nodoPadre.getClave()) < 0 ? "I" : "D") + "]");

            String prefijoIzquierdo = prefijo + (nodo.getHijoDerecho() != null ? "│   " : "    ");
            String prefijoDerecho = prefijo + "    ";

            imprimirArbol(nodo.getHijoIzquierdo(), prefijoIzquierdo, true, nodo);
            imprimirArbol(nodo.getHijoDerecho(), prefijoDerecho, false, nodo);
        }
    }

    /*METODOS PRACTICA  PARA EL EXAMEN */
    //---------- CANTIDAD DE NODOS HOJAS EN UN NIVEL PREDERMINADO --------------
    public int cantidadDeNodosHojasEnUnNivel(int nivel) {
        return this.cantidadDeNodosHojasEnUnNivel(raiz, 0, nivel);
    }

    private int cantidadDeNodosHojasEnUnNivel(NodoBinario<K, V> nodoActual, int nivelEsperado, int nivelABuscar) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        if (nivelEsperado == nivelABuscar && nodoActual.esHoja()) {
            return 1;
        }
        int cantidadIzquierda = cantidadDeNodosHojasEnUnNivel(nodoActual.getHijoIzquierdo(), nivelEsperado + 1, nivelABuscar);
        int cantidadDerecha = cantidadDeNodosHojasEnUnNivel(nodoActual.getHijoDerecho(), nivelEsperado + 1, nivelABuscar);

        return cantidadDerecha + cantidadIzquierda;
    }

    //------------ CANTIDAD DE NODOS EN UN NIVEL ESPECIFICO --------------------
    public int cantidadDeNodosEnUnNivel(int nivel) {
        return cantidadDeNodosEnUnNivel(raiz, 0, nivel);
    }

    private int cantidadDeNodosEnUnNivel(NodoBinario<K, V> nodoActual, int nivelEsperado, int nivelABuscar) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        if (nivelEsperado == nivelABuscar) {
            return 1;
        }
        int cantidadIzquierda = cantidadDeNodosEnUnNivel(nodoActual.getHijoIzquierdo(), nivelEsperado + 1, nivelABuscar);
        int cantidadDerecha = cantidadDeNodosEnUnNivel(nodoActual.getHijoDerecho(), nivelEsperado + 1, nivelABuscar);

        return cantidadDerecha + cantidadIzquierda;
    }

    //---------------- ES ARBOL Lleno ---------------------------------------
    public boolean esArbolLleno() {
        for (int i = 0; i < this.nivel(); i++) {
            int cantidadDeHijoFormula = (int) Math.pow(2, i);
            int cantidadDeHijos = this.cantidadDeNodosEnUnNivel(i);
            if (cantidadDeHijoFormula != cantidadDeHijos) {
                return false;
            }
        }
        return true;
    }

    //-------------- ES MONTICULO ----------------------------------------------
    public boolean esMonticulo() {
        if (this.esArbolVacio()) {
            return false;
        }
        Queue<NodoBinario<K, V>> colaNodos = new LinkedList<>();
        colaNodos.add(raiz);
        while (!colaNodos.isEmpty()) {
            NodoBinario<K, V> nodoActual = colaNodos.poll();
            if (!nodoActual.esHijoIzquierdoVacio()) {
                K claveActual = nodoActual.getClave();
                K claveHijo = nodoActual.getHijoIzquierdo().getClave();
                if (claveHijo.compareTo(claveActual) >= 0) {
                    return false;
                }
                colaNodos.offer(nodoActual.getHijoIzquierdo());
            }
            if (!nodoActual.esHijoDerechoVacio()) {
                K claveActual = nodoActual.getClave();
                K claveHijo = nodoActual.getHijoDerecho().getClave();
                if (claveHijo.compareTo(claveActual) >= 0) {
                    return false;
                }
                colaNodos.offer(nodoActual.getHijoDerecho());
            }
        }
        return true;
    }

    //----------- ES EQUIVALENTE A OTRO ARBOL ----------------------------------
    public boolean esEquivalente(ArbolBinarioBusqueda<K, V> otroArbol) {
        if (this.esArbolVacio() && otroArbol.esArbolVacio()) {
            return false;
        }
        if (this.size() != otroArbol.size()) {
            return false;
        }
        Stack<NodoBinario<K, V>> pilaNodoA = new Stack<>();
        Stack<NodoBinario<K, V>> pilaNodoB = new Stack<>();
        pilaNodoA.push(raiz);
        pilaNodoB.push(otroArbol.raiz);
        while (!pilaNodoA.isEmpty()) {
            NodoBinario<K, V> nodoActualA = pilaNodoA.pop();
            NodoBinario<K, V> nodoActualB = pilaNodoB.pop();

            if (nodoActualA.getClave() != nodoActualB.getClave()) {
                return false;
            }
            //---------si llegamos aca indica que son iguales los nodo--------
            if (!nodoActualA.esHijoDerechoVacio()) {
                if (nodoActualB.esHijoDerechoVacio()) {
                    return false;
                }
                pilaNodoA.push(nodoActualA.getHijoDerecho());
                pilaNodoB.push(nodoActualB.getHijoDerecho());
            }
            if (!nodoActualA.esHijoIzquierdoVacio()) {
                if (nodoActualB.esHijoIzquierdoVacio()) {
                    return false;
                }
                pilaNodoA.push(nodoActualA.getHijoIzquierdo());
                pilaNodoB.push(nodoActualB.getHijoIzquierdo());
            }

        }
        return true;
    }
}
