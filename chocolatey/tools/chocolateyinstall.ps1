$version = '3.0.0-M5'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'ED783AF77D6649E05C69678B4BE004C6B637156D7BE9AE7115771FC3785BE307'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
