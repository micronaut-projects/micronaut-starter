$version = '4.10.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '3D04602E0DFDF911DD9D3BC8F0133BE6F527E5CA8F6D6DDCB7EBBB889D82D666'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
