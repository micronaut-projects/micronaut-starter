$version = '5.1.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'CECF072AB337338C9D6717B6923C23D835B77AB72BB5027176EE0ABFA2E29D85'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
