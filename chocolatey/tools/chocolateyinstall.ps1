$version = '5.0.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'F3F414A02272C9C24ABAEF75A0D052DBFBD5C76B93735E978E68EC5A87CD163C'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
