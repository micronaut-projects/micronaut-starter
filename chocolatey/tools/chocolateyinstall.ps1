$version = '3.0.0-RC1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '8A5837F87A8403F69F8DE2DCF42A3B61A1831DF8F16F0774F887CAA2D20489C7'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
