// lib/components/search_bar.dart
import 'package:flutter/material.dart';

class SearchBar extends StatelessWidget {
  final String? departure;
  final String? arrival;
  final VoidCallback onDepartureClick;
  final VoidCallback onArrivalClick;
  final VoidCallback onButtonClick;

  const SearchBar({
    Key? key,
    this.departure,
    this.arrival,
    required this.onDepartureClick,
    required this.onArrivalClick,
    required this.onButtonClick,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        // Departure Field
        ListTile(
          leading: Icon(Icons.departure_board),
          title: Text(departure ?? 'Select Departure'),
          onTap: onDepartureClick,
        ),
        // Arrival Field
        ListTile(
          leading: Icon(Icons.arrival_board),
          title: Text(arrival ?? 'Select Arrival'),
          onTap: onArrivalClick,
        ),
        // Search Button
        ElevatedButton(
          onPressed: onButtonClick,
          child: Text('Find Path'),
        ),
      ],
    );
  }
}
