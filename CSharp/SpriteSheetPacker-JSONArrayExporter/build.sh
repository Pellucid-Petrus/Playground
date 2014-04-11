#!/bin/bash
gmcs /reference:System.Drawing.dll JSONExporter.cs /reference:sspack.exe -t:library JSONExporter.cs
