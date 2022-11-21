package com.grupom.pm1examen2grupo2.Config;

public class RestApiMethods {
    //public static final String ipaddress = "18.188.250.246";
    public static final String ipaddress = "18.188.250.246";

    public static String webApi = "/PM01EX2";

    //Rests
    private static final String GetList = "/listaContactos.php";
    private static final String FindContact = "/listasinglecontacto.php?nombre=";
    private static final String UpdateContact = "/actualizarcontacto.php";
    private static final String CreateContact = "/crearContacto.php";

    //Llamados a la api
    public static final String apiGetList =  "http://"+ ipaddress + webApi + GetList;
    public static final String apiFindContact = "http://"+ipaddress + webApi + FindContact;
    public static final String apiUpdateContact = "http://"+ipaddress + webApi + UpdateContact;
    public static final String apiCreateContact = "http://"+ ipaddress + webApi + CreateContact;

}
