$version = '4.10.10'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '8432DA18114BD71B6B0D2D3E169790297B24037C0DEB908C0974B85631387640'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
