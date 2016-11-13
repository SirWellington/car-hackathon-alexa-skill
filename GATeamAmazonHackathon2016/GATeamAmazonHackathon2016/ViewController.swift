//
//  ViewController.swift
//  GATeamAmazonHackathon2016
//
//  Created by Cordero Hernandez on 11/12/16.
//  Copyright © 2016 Cordero Hernandez. All rights reserved.
//

import UIKit
import Foundation
import CoreLocation
import MapKit

class ViewController: UIViewController, CLLocationManagerDelegate {

    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        
        guard let delegate = UIApplication.shared.delegate as? AppDelegate else { return }
        
        delegate.onNewLocation = { newLocation in
            //            self.openLocationAt(newLocation)
            print(newLocation)
            
            let newPlaceMark = MKPlacemark(coordinate: newLocation)
            let mapItem2 = MKMapItem(placemark: newPlaceMark)
            let options2 = [MKLaunchOptionsDirectionsModeKey:
                MKLaunchOptionsDirectionsModeDriving,
                           MKLaunchOptionsShowsTrafficKey: true] as [String : Any]
            mapItem2.openInMaps(launchOptions: options2)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
