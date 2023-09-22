$version = '4.1.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '511BA7C1F3EBD4758A97D16A655C48C9912854D91C4B0B26E592AC35470A6B4C'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
