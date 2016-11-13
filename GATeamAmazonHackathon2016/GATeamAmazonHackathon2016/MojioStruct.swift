//
//  MojiStruct.swift
//  GATeamAmazonHackathon2016
//
//  Created by Cordero Hernandez on 11/12/16.
//  Copyright © 2016 Cordero Hernandez. All rights reserved.
//

import Foundation
import UIKit

struct Mojio {
    
    let location: NSDictionary
    let lat: Double
    let long: Double
    
    
    static func from(dictionary: NSDictionary) -> Mojio? {
        
        guard let location = dictionary ["Location"] as? NSDictionary,
        let lat = dictionary ["Lat"] as? NSDictionary,
            let long = dictionary ["Lng"] as? NSDictionary else { return nil }
        
        return Mojio(location: location, lat: lat, long: long)
        
        
    }
    
    
}
