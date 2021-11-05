$version = '3.1.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'DC4941316710F27DE93D2627C80668782C4A3E90E569C59030149FE37422DB81'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
