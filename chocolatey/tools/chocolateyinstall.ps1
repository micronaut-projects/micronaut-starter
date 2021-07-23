$version = '2.5.11'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '3ACAD78A4AD5460B2744A8D80644667D2D7A3B75CBCEBC944344B20300773065'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
