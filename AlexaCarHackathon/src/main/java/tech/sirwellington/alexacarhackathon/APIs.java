/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 
package tech.sirwellington.alexacarhackathon;


import com.notnoop.apns.ApnsService;
import tech.aroma.client.Aroma;
import tech.sirwellington.alchemy.http.AlchemyHttp;
import tech.sirwellington.alexacarhackathon.notifications.Certificates;

/**
 *
 * @author SirWellington
 */
public final class APIs 
{
    public static final Aroma AROMA = Aroma.create("d4592e6b-cb53-4dea-945d-52043abbec84");

    public static AlchemyHttp HTTP = AlchemyHttp.newDefaultInstance();

    public static ApnsService APNS = Certificates.createPushService();
}
