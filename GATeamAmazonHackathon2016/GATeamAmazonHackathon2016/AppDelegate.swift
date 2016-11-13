//
//  AppDelegate.swift
//  GATeamAmazonHackathon2016
//
//  Created by Cordero Hernandez on 11/12/16.
//  Copyright © 2016 Cordero Hernandez. All rights reserved.
//

import UIKit
import UserNotifications
import CoreLocation
import MapKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate {
    
    var window: UIWindow?
    
    var onNewLocation: ((CLLocationCoordinate2D) -> Void)? = nil
    
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        
        requestPushNotificationsAccess(for: application)
        return true
    }
    
    func requestPushNotificationsAccess(for application: UIApplication) {
        
        if #available(iOS 10, *) {
            let center = UNUserNotificationCenter.current()
            center.requestAuthorization(options: [.alert], completionHandler: self.handleAuthorization)
            center.delegate = self
        } else {
            registerPushNotifications(for: application)
        }
    }
    
    private func registerPushNotifications(for application: UIApplication) {
        
        let settings = UIUserNotificationSettings(types: [.alert, .badge], categories: nil)
        application.registerUserNotificationSettings(settings)
        application.registerForRemoteNotifications()
    }
    
    private func handleAuthorization(granted: Bool, error: Error?) {
        
        let application = UIApplication.shared
        registerPushNotifications(for: application)
    }
    
    @available(iOS 10, *)
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        completionHandler()
    }
    
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        
        let characterSet: NSCharacterSet = NSCharacterSet(charactersIn: " ")
        
        let deviceTokenString: String = deviceToken.base64EncodedString()
        print("Token: \(deviceTokenString)")
        
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        print("data: \(userInfo)")
        
        guard let json = userInfo["json"] as? String else {
            return
        }
    
        //print(json)
        
        
       guard let data = json.data(using: .utf8),
            let jsonCoordinates = try? JSONSerialization.jsonObject(with: data, options: [])
        else {
            print ("Failed to DeSerialize the data")
            return
        }
        
        guard let jsonDictionary = jsonCoordinates as? NSDictionary,
        let latitude = jsonDictionary["latitude"] as? Double,
        let longitude = jsonDictionary["longitude"] as? Double
        else {
                return
        }
        
        let coordinate = CLLocationCoordinate2DMake(latitude, longitude)
//        onNewLocation?(coordinate)
        
        openLocation(coordinate)
        
        print("Here we go: \(jsonDictionary)")
        
        
        
        completionHandler(.newData)
        
    
    }
    
    private func openLocation(_ coordinate: CLLocationCoordinate2D) {
        
        let newPlaceMark = MKPlacemark(coordinate: coordinate)
        let mapItem2 = MKMapItem(placemark: newPlaceMark)
        let options2 = [MKLaunchOptionsDirectionsModeKey:
            MKLaunchOptionsDirectionsModeDriving,
                        MKLaunchOptionsShowsTrafficKey: true] as [String : Any]
        
        mapItem2.openInMaps(launchOptions: options2)
    }
    
    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }
    
    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }
    
    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }
    
    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }
    
    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
    
    
}

