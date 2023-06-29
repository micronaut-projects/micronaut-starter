$version = '4.0.0-M5'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '277CB0C9A6D85DCAFFE9AEE43AF1F8AFF1554DB96BE39ACDE7D88443AC758E6A'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
