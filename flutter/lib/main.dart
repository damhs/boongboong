// lib/main.dart
import 'package:flutter/material.dart';
import 'pages/search_page.dart';

void main() {
  runApp(ShoongApp());
}

class ShoongApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Shoong App',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: SearchPage(),
    );
  }
}
