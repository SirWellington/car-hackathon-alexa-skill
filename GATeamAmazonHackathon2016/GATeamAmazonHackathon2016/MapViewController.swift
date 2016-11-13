//
//  ViewController.swift
//  GATeamAmazonHackathon2016
//
//  Created by Cordero Hernandez on 11/12/16.
//  Copyright © 2016 Cordero Hernandez. All rights reserved.
//

import UIKit
import MapKit
import CoreLocation


class MapViewController: UIViewController, CLLocationManagerDelegate, MKMapViewDelegate {
    
    @IBOutlet weak var mapView: MKMapView!
    var locationManager: CLLocationManager!
    
    var urlString: String = ""
    var mojioObject: [Mojio] = []
    
    private let async: OperationQueue = {
        
        let operationQ = OperationQueue()
        operationQ.maxConcurrentOperationCount = 10
        
        return operationQ
    }()
    
    func connectToMojio() {
        
        urlString = "https://api.moj.io/v2/vehicles"
        
        let url = URL(string: urlString)!
        
        var request = URLRequest.init(url: url)
        
        request.setValue("bearer 02336590-2349-4ca5-9038-3ced9fd9098d", forHTTPHeaderField: "Authorization")
        
        let task = URLSession.shared
        
        let dataTask = task.dataTask(with: request) { (data, response, error) -> Void in
            
            guard let stillData = data else { return }
            
            guard let objectData = try? JSONSerialization.jsonObject(with: stillData, options: []) as! NSDictionary
                else {return}
            
            guard let arrayOfInformation = objectData["vehicles"] as? NSArray
                else { return }
            
            
            
            
        }
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        locationManager = CLLocationManager()
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.distanceFilter = kCLDistanceFilterNone
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
        
        mapView.delegate = self
        mapView.showsUserLocation = true
        
    }
    
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        let userLocation: CLLocation = locations[0]
        var latitude = userLocation.coordinate.latitude
        var longitude = userLocation.coordinate.longitude
        let latDelta: CLLocationDegrees = 0.05
        let longDelta: CLLocationDegrees = 0.05
        let span = MKCoordinateSpanMake(latDelta, longDelta)
        let theLocation = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
        let region = MKCoordinateRegionMake(theLocation, span)
        
        self.mapView.setRegion(region, animated: true)
        latitude = theLocation.latitude
        longitude = theLocation.longitude
        locationManager.startUpdatingLocation()
        print ("Coordinates: \(latitude) and \(longitude)")
    }
    
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
}

