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
    
    let location: String
    let lat: Double
    let long: Double
    
    
    static func from(dictionary: NSDictionary) -> Mojio? {
        
        guard let location = dictionary ["Location"] as? String,
        let lat = dictionary ["Lat"] as? Double,
            let long = dictionary ["Lng"] as? Double else { return nil }
        
        return Mojio(location: location, lat: lat, long: long)
        
        
    }
    
    
}
