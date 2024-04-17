$version = '4.4.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '76DFFE1D64ECF9986E8D1E9B3CC7F6C936543170ED626F1BB5A45560A924DA1C'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
