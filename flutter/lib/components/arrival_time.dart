// lib/components/arrival_time.dart
import 'package:flutter/material.dart';

class ArrivalTime extends StatelessWidget {
  // Placeholder for arrival time data
  final String? arrivalTime;

  const ArrivalTime({Key? key, this.arrivalTime}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return arrivalTime != null
        ? ListTile(
            leading: Icon(Icons.access_time),
            title: Text('Estimated Arrival: $arrivalTime'),
          )
        : Container();
  }
}
