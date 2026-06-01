$version = '5.0.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'CAA46F437B3D093F269A82832CB95DEEEDDAFD88E70C6F9DF621FFCE202B8F9A'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
