$version = '4.3.6'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'A9E7D4E7E2AD3368E0E26976F318DFCB998D43ECB46812F5CDA34D598A176000'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
