// lib/components/header.dart
import 'package:flutter/material.dart';

class Header extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return AppBar(
      title: Text('Search'),
      centerTitle: true,
      backgroundColor: Colors.blue,
    );
  }
}
