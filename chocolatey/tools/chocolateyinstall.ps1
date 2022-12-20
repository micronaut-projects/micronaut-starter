$version = '3.7.5'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '5BA09860ECDEAE893CD3F35F6317699DB540DB56FDAE60E9DB7CF3C48BEB7A43'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
